package cn.along.jraft.monitor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author zhengjianglong
 * @since 2019-02-01
 */
public class Monitor {
    private ScheduledExecutorService service = Executors.newScheduledThreadPool(1);


    public void start() {
        service.scheduleAtFixedRate(new HeartBeatMonitor(), 0, 1, TimeUnit.SECONDS);
    }
}
