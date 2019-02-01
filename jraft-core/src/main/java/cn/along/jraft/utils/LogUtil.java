package cn.along.jraft.utils;

/**
 * @author zhengjianglong
 * @since 2019-02-01
 */
public class LogUtil {

    public static void info(String msg) {
        System.out.println(msg);
    }

    public static void info(String format, Object... val) {
        System.out.println(String.format(format, val));
    }

    public static void error(Throwable e, String msg, Object... val) {
        System.err.println(String.format(msg, val));
        e.printStackTrace();
    }
}
