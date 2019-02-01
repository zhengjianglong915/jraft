package cn.along.jraft.processor;

import cn.along.jraft.common.command.VoteCommand;
import cn.along.jraft.common.result.BaseResult;
import cn.along.jraft.manager.VoteMananger;
import cn.along.jraft.utils.LocalNode;
import cn.along.jraft.utils.LogUtil;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.SyncUserProcessor;

/**
 * @author zhengjianglong
 * @since 2019-02-01
 */
public class VoteProcessor extends SyncUserProcessor<VoteCommand> {

    @Override
    public BaseResult handleRequest(BizContext bizContext, VoteCommand voteCommand) throws Exception {
        LogUtil.info("[VoteProcessor]  received %s %s", voteCommand.getTerm(),
                voteCommand.getServerAddress());
        long term = voteCommand.getTerm();
        if (VoteMananger.voteMap.containsKey(term)
                && !VoteMananger.voteMap.get(term).equals(voteCommand.getServerAddress())) {
            if (LocalNode.isLeader()) {
                LocalNode.node.sendHeartBeat(voteCommand.getServerAddress());
            }
            return BaseResult.failed("");
        } else {
            VoteMananger.voteMap.put(term, voteCommand.getServerAddress());
        }
        return BaseResult.success();
    }

    @Override
    public String interest() {
        return VoteCommand.class.getName();
    }
}
