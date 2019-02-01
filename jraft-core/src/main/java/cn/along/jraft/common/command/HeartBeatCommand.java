package cn.along.jraft.common.command;

/**
 * @author zhengjianglong
 * @since 2019-02-01
 */
public class HeartBeatCommand extends BaseCommand {
    private String serverAddress;

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }
}
