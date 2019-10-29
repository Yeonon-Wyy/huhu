package top.yeonon.huhucommon.exception;

import lombok.Data;

/**
 * @Author yeonon
 * @date 2019/4/9 0009 19:35
 **/
@Data
public class HuhuException extends Exception {

    private int code;

    public HuhuException(int code, String message) {
        super(message);
        this.code = code;
    }
}
