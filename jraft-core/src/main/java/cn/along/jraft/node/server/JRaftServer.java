package cn.along.jraft.node.server;

import cn.along.jraft.processor.*;
import cn.along.jraft.utils.LogUtil;
import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.rpc.RpcServer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author zhengjianglong
 * @since 2019-02-01
 */
public class JRaftServer {
    private static RpcServer rpcServer;

    private AtomicBoolean inited = new AtomicBoolean(false);
    private int port;

    public JRaftServer(int port) {
        this.port = port;
    }

    public void init() {
        synchronized (inited) {
            if (inited.get()) {
                return;
            }
            // 1.
            rpcServer = new RpcServer(port);

            //
            rpcServer.addConnectionEventProcessor(ConnectionEventType.CONNECT, new ConnectionProcessor());
            rpcServer.addConnectionEventProcessor(ConnectionEventType.CLOSE, new DisConnectionProcessor());

            //
            rpcServer.registerUserProcessor(new JoinClusterProcessor());
            rpcServer.registerUserProcessor(new HeartBeatProcessor());
            rpcServer.registerUserProcessor(new VoteProcessor());

            if (rpcServer.start()) {
                LogUtil.info("[rpcServer] port=%s, start success", port);
            } else {
                LogUtil.info("[rpcServer] port=%s, start failed", port);
            }

            inited.set(true);
        }
    }
}
