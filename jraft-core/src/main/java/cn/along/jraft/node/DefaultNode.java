package cn.along.jraft.node;

import cn.along.jraft.common.NodeStatus;
import cn.along.jraft.common.command.HeartBeatCommand;
import cn.along.jraft.common.command.JoinClusterCommand;
import cn.along.jraft.common.command.VoteCommand;
import cn.along.jraft.common.result.BaseResult;
import cn.along.jraft.manager.ConnManager;
import cn.along.jraft.manager.VoteMananger;
import cn.along.jraft.node.client.JRaftClient;
import cn.along.jraft.node.server.JRaftServer;
import cn.along.jraft.utils.Cluster;
import cn.along.jraft.utils.LocalNode;
import cn.along.jraft.utils.LogUtil;
import com.alipay.remoting.Connection;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhengjianglong
 * @since 2019-02-01
 */
public class DefaultNode implements Node {
    private JRaftServer server;
    private JRaftClient client;

    private Random random;

    public DefaultNode(int port) {
        server = new JRaftServer(port);
        server.init();

        // status
        LocalNode.STATUS.set(NodeStatus.FOLLOWER);
        client = new JRaftClient();

        random = new Random();
        random.setSeed(System.currentTimeMillis());
    }

    @Override
    public void onStart() {
        List<String> serverList = Cluster.getServerList();
        if (serverList != null) {
            for (String address : serverList) {
                if (LocalNode.ADDRESS.equals(address)) {
                    continue;
                }

                Connection connection = client.connect(address);
                if (connection != null) {
                    ConnManager.addConnection(address, connection);
                }
            }
        }
    }

    @Override
    public void onJoin(String address) {
        Connection connection = client.connect(address);
        ConnManager.addConnection(address, connection);
    }

    @Override
    public void joinCluster() {
        Map<String, Connection> connectionMap = ConnManager.getAllConnect();
        LogUtil.info("joinCluster %s", connectionMap.size());
        connectionMap.forEach((address, connection) -> {
            try {
                JoinClusterCommand command = new JoinClusterCommand();
                command.setServerAddress(LocalNode.ADDRESS);
                client.invokeSync(connection, command);
            } catch (Exception e) {
                e.printStackTrace();
                ConnManager.removeByServer(address);
            }
        });
    }

    @Override
    public void onElect() {
        int waitTime = random.nextInt(500) + 1;
        try {
            Thread.sleep(waitTime);

        } catch (Exception e) {
            LogUtil.error(e, "[onElect] elect exception.");
        }
        VoteMananger.increment();
        long term = VoteMananger.term.get();
        VoteMananger.voteMap.put(term, LocalNode.ADDRESS);
        Map<String, Connection> connectionMap = ConnManager.getAllConnect();

        AtomicInteger successCount = new AtomicInteger(0);
        successCount.incrementAndGet();
        connectionMap.forEach((address, connection) -> {
            try {
                VoteCommand command = new VoteCommand();
                command.setTerm(term);
                command.setServerAddress(LocalNode.ADDRESS);
                BaseResult baseResult = (BaseResult) client.invokeSync(connection, command);
                if (baseResult.isSuccess()) {
                    successCount.incrementAndGet();
                }
                LogUtil.info("[elect] result from %s %s", address, baseResult.getCode());
            } catch (Exception e) {
                e.printStackTrace();
                ConnManager.removeByServer(address);
            }
        });
        LogUtil.info("[elect] result %s %s ", successCount, connectionMap.size());

        if (successCount.get() > (connectionMap.size() + 1 ) / 2) {
            LogUtil.info("[elect] elect success %s", LocalNode.ADDRESS);
            LocalNode.STATUS.set(NodeStatus.LEADER);
            LocalNode.node.sendHeartBeat();
        }
    }

    @Override
    public void sendHeartBeat() {
        Map<String, Connection> connectionMap = ConnManager.getAllConnect();
        connectionMap.forEach((address, connection) -> {
            try {
                HeartBeatCommand command = new HeartBeatCommand();
                command.setServerAddress(LocalNode.ADDRESS);
                client.invokeSync(connection, command);
            } catch (Exception e) {
                ConnManager.removeByServer(address);
            }
        });
    }

    @Override
    public void sendHeartBeat(String address) {
        Map<String, Connection> connectionMap = ConnManager.getAllConnect();
        try {
            HeartBeatCommand command = new HeartBeatCommand();
            command.setServerAddress(LocalNode.ADDRESS);
            client.invokeSync(connectionMap.get(address), command);
        } catch (Exception e) {
            ConnManager.removeByServer(address);
        }
    }
}
