package cn.along.jraft.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhengjianglong
 * @since 2019-02-01
 */
public class VoteMananger {
    public static AtomicInteger term = new AtomicInteger(0);
    public static Map<Long, String> voteMap = new ConcurrentHashMap<>();

    public static void increment() {
        term.incrementAndGet();
    }
}
