package cn.along.jraft.processor;

import cn.along.jraft.common.NodeStatus;
import cn.along.jraft.common.command.HeartBeatCommand;
import cn.along.jraft.common.result.BaseResult;
import cn.along.jraft.monitor.HeartBeatMonitor;
import cn.along.jraft.utils.LocalNode;
import cn.along.jraft.utils.LogUtil;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.SyncUserProcessor;

import java.util.Date;

/**
 * @author zhengjianglong
 * @since 2019-02-01
 */
public class HeartBeatProcessor extends SyncUserProcessor<HeartBeatCommand> {

    @Override
    public BaseResult handleRequest(BizContext bizContext, HeartBeatCommand command) throws Exception {
        LogUtil.info("[HeartBeatProcessor] received heartbeat from %s.", command.getServerAddress());

        LocalNode.STATUS.set(NodeStatus.FOLLOWER);
        HeartBeatMonitor.lastHeatBeatTime.set(new Date());
        return BaseResult.success();
    }

    @Override
    public String interest() {
        return HeartBeatCommand.class.getName();
    }
}
