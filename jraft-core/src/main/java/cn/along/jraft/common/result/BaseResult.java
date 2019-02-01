package cn.along.jraft.common.result;

import java.io.Serializable;

/**
 * @author zhengjianglong
 * @since 2019-02-01
 */
public class BaseResult implements Serializable {
    private String SUCCESS_CORE = "0";
    private String code;
    private String codeMsg;

    public BaseResult() {
        code = SUCCESS_CORE;
        codeMsg = "SUCCESS";
    }

    public static BaseResult success() {
        BaseResult result = new BaseResult();
        return result;
    }

    public static BaseResult failed(String msg) {
        BaseResult result = new BaseResult();
        result.setCode("1");
        result.setCodeMsg(msg);
        return result;
    }

    public boolean isSuccess() {
        return code.equals("0");
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeMsg() {
        return codeMsg;
    }

    public void setCodeMsg(String codeMsg) {
        this.codeMsg = codeMsg;
    }
}
