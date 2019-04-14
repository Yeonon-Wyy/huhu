package top.yeonon.huhuuserservice.constants;

import lombok.Getter;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 15:16
 **/
@Getter
public enum UserStatus {

    CLOSE(-1, "账号注销"),
    NORMAL(0, "正常"),
    MUTE(1, "禁用"),
    LANDED(2, "禁止登陆");

    private int code;

    private String description;

    UserStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
