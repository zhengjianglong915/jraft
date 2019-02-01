package cn.along.jraft.manager;

import com.alipay.remoting.Connection;
import com.alipay.remoting.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhengjianglong
 * @since 2019-02-01
 */
public class ConnManager {

    /**
     * client address (ip:port) --> server address(ip:port)
     */
    private static final Map<String, String> clientServerMap = new ConcurrentHashMap<>();
    private static final Map<String, String> serverClientMap = new ConcurrentHashMap<>();

    /**
     * each server conncetion
     * server address --> connection
     */
    private static final Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    /**
     * add
     *
     * @param clientAddress
     * @param serverAddress
     */
    public static void putIfAbsent(String clientAddress, String serverAddress) {
        if (serverClientMap.containsKey(clientAddress)) {
            clientServerMap.put(clientAddress, serverAddress);
            serverClientMap.put(serverAddress, clientAddress);
        }
    }

    /**
     * remove
     *
     * @param clientAddress
     */
    public static void remove(String clientAddress) {
        String server = serverClientMap.get(clientAddress);
        if (StringUtils.isNotEmpty(server)) {
            connectionMap.remove(server);
            serverClientMap.remove(server);
        }
        clientServerMap.remove(clientAddress);
    }

    public static void removeByServer(String address) {
        String clientAddress = serverClientMap.get(address);
        if (StringUtils.isNotEmpty(clientAddress)) {
            clientServerMap.remove(clientAddress);
        }
        serverClientMap.remove(address);
        connectionMap.remove(address);
    }

    public static void addConnection(String serverAddress, Connection connection) {
        connectionMap.put(serverAddress, connection);
    }

    /**
     * get call
     *
     * @return
     */
    public static Map<String, Connection> getAllConnect() {
        Map<String, Connection> map = new HashMap<>(connectionMap);
        return map;
    }
}
