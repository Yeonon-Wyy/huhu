package top.yeonon.huhucommon.response;

import lombok.Getter;

/**
 * @Author yeonon
 * @date 2019/4/9 0009 19:33
 **/
@Getter
public enum ResponseCode {

    SUCCESS(0, "SUCCESS"),
    PARAM_ERROR(1, "PARAM_ERROR"),
    SERVICE_ERROR(2, "SERVICE_ERROR");

    private int code;

    private String description;

    ResponseCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
