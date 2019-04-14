package top.yeonon.huhuuserservice.constants;

import lombok.Getter;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 15:22
 **/
@Getter
public enum UserSex {

    UNKNOWN(-1, "未知"),
    MALE(0, "男性"),
    FEMALE(1, "女性");


    private int code;

    private String description;

    UserSex(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
