package cn.along.jraft;

import cn.along.jraft.monitor.HeartBeatMonitor;
import cn.along.jraft.node.DefaultNode;
import cn.along.jraft.utils.Cluster;
import cn.along.jraft.utils.LocalNode;
import cn.along.jraft.utils.LogUtil;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author zhengjianglong
 * @since 2019-02-01
 */
public class JRaftBootstrap {
    private DefaultNode serverNode;

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public JRaftBootstrap(int port, List<String> serverList) {
        init(port);

        Cluster.setServerList(serverList);

        serverNode = new DefaultNode(port);
        LocalNode.node = serverNode;

        // start
        serverNode.onStart();

        // join cluster
        serverNode.joinCluster();

        HeartBeatMonitor monitor = new HeartBeatMonitor();
        new Thread(monitor).run();
    }

    public void init(int port) {
        try {
            InetAddress address = InetAddress.getLocalHost();
            LocalNode.ADDRESS = address.getHostAddress() + ":" + port;
        } catch (Exception e) {
            LogUtil.error(e, "[JRaftBootstrap] init fail");
        }
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        try {
            InetAddress address = InetAddress.getLocalHost();
            list.add(address.getHostAddress() + ":9000");
            list.add(address.getHostAddress() + ":9001");
            list.add(address.getHostAddress() + ":9002");
        } catch (Exception e) {
            LogUtil.error(e, "[JRaftBootstrap] init fail");
        }

        new JRaftBootstrap(9000, list);
    }
}
