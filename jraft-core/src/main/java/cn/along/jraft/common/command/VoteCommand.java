package cn.along.jraft.common.command;

import cn.along.jraft.common.command.BaseCommand;

/**
 * @author zhengjianglong
 * @since 2019-02-01
 */
public class VoteCommand extends BaseCommand {
    private long term;
    private String serverAddress;

    public long getTerm() {
        return term;
    }

    public void setTerm(long term) {
        this.term = term;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }
}
