package cn.along.jraft.node;

/**
 * @author zhengjianglong
 * @since 2019-02-01
 */
public interface Node {

    /**
     * 启动
     */
    void onStart();

    /**
     *
     */
    void onJoin(String address);

    /**
     *
     */
    void joinCluster();

    /**
     * elect
     */
    void onElect();

    /**
     * send heart beat, for all
     */
    void sendHeartBeat();


    void sendHeartBeat(String address);
}
