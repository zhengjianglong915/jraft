package cn.along.jraft.monitor;

import cn.along.jraft.common.NodeStatus;
import cn.along.jraft.utils.LocalNode;
import cn.along.jraft.utils.LogUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zhengjianglong
 * @since 2019-02-01
 */
public class HeartBeatMonitor implements Runnable {
    public static AtomicReference<Date> lastHeatBeatTime = new AtomicReference<>(new Date());

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
            LogUtil.info("[monitor] %s %s", LocalNode.STATUS.get(), LocalNode.ADDRESS);
            if (LocalNode.isLeader()) {
                LocalNode.node.sendHeartBeat();
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MILLISECOND, -1500);
                Date timeoutTime = calendar.getTime();
                if (lastHeatBeatTime.get().before(timeoutTime)) {
                    LocalNode.STATUS.set(NodeStatus.CANDIDATE);
                    LocalNode.node.onElect();
                }
            }
        }
    }
}
