package cn.along.jraft.utils;

import cn.along.jraft.common.NodeStatus;
import cn.along.jraft.node.Node;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zhengjianglong
 * @since 2019-02-01
 */
public class LocalNode {
    public static String ADDRESS;
    public static AtomicReference<NodeStatus> STATUS = new AtomicReference<>();

    public static Node node;

    /**
     * is leader
     *
     * @return
     */
    public static boolean isLeader() {
        return STATUS.get().equals(NodeStatus.LEADER);
    }

}
