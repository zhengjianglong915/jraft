package cn.along.jraft.processor;

import cn.along.jraft.utils.LogUtil;
import com.alipay.remoting.Connection;
import com.alipay.remoting.ConnectionEventProcessor;

/**
 * @author zhengjianglong
 * @since 2019-02-01
 */
public class ConnectionProcessor implements ConnectionEventProcessor {

    @Override
    public void onEvent(String address, Connection connection) {
        LogUtil.info("[DisConnectionProcessor] %s", address);
    }
}
