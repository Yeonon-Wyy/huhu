package top.yeonon.huhumailservice.constants;

import lombok.Getter;

/**
 * @Author yeonon
 * @date 2019/4/17 0017 19:37
 **/
@Getter
public enum MailType {

    FORGET_PASSWORD(1, "forget_password.html");

    private int code;

    private String templateName;

    MailType(int code, String templateName) {
        this.code = code;
        this.templateName = templateName;
    }
}
