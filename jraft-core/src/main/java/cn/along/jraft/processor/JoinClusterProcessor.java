package cn.along.jraft.processor;

import cn.along.jraft.common.command.JoinClusterCommand;
import cn.along.jraft.common.result.BaseResult;
import cn.along.jraft.manager.ConnManager;
import cn.along.jraft.utils.LocalNode;
import cn.along.jraft.utils.LogUtil;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.SyncUserProcessor;
import com.alipay.remoting.util.StringUtils;

/**
 * @author zhengjianglong
 * @since 2019-02-01
 */
public class JoinClusterProcessor extends SyncUserProcessor<JoinClusterCommand> {

    @Override
    public BaseResult handleRequest(BizContext bizContext, JoinClusterCommand command) throws Exception {
        String clientAddress = bizContext.getRemoteAddress() + ":" + bizContext.getRemotePort();

        if (StringUtils.isEmpty(command.getServerAddress())) {
            LogUtil.info("[JoinClusterProcessor] %s join cluster failed.", clientAddress);
            return BaseResult.failed("server address should't be empty");
        }

        ConnManager.putIfAbsent(clientAddress, command.getServerAddress());
        LocalNode.node.onJoin(command.getServerAddress());
        if (LocalNode.isLeader()) {
            // 发送心跳
            LocalNode.node.sendHeartBeat(command.getServerAddress());
        }
        LogUtil.info("[JoinClusterProcessor] %s join cluster success", clientAddress);
        return BaseResult.success();
    }

    @Override
    public String interest() {
        return JoinClusterCommand.class.getName();
    }
}
