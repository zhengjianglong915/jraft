package cn.along.jraft.utils;


import java.util.List;

/**
 * @author zhengjianglong
 * @since 2019-02-01
 */
public class Cluster {

    private static List<String> serverList;


    public static void setServerList(List<String> serverList) {
        Cluster.serverList = serverList;
    }

    public static List<String> getServerList() {
        return serverList;
    }
}
