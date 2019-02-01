package cn.along.jraft.node.client;

import cn.along.jraft.common.command.BaseCommand;
import cn.along.jraft.processor.ConnectionProcessor;
import cn.along.jraft.processor.DisConnectionProcessor;
import cn.along.jraft.utils.LogUtil;
import com.alipay.remoting.Connection;
import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.rpc.RpcClient;

/**
 * @author zhengjianglong
 * @since 2019-02-01
 */
public class JRaftClient {

    private RpcClient rpcClient;

    public JRaftClient() {
        rpcClient = new RpcClient();

        rpcClient.addConnectionEventProcessor(ConnectionEventType.CONNECT, new ConnectionProcessor());
        rpcClient.addConnectionEventProcessor(ConnectionEventType.CLOSE, new DisConnectionProcessor());

        rpcClient.init();
    }

    /**
     * connect
     *
     * @param address
     * @return
     */
    public Connection connect(String address) {
        try {
            return rpcClient.getConnection(address, 2000);
        } catch (Exception e) {
            LogUtil.info("[rpcClient] fail to connect %s", address);
        }
        return null;
    }

    public Object invokeSync(Connection connection, BaseCommand command) throws Exception {
        return rpcClient.invokeSync(connection, command, 3000);
    }
}
