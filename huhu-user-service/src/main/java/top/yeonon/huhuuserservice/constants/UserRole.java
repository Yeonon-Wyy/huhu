package top.yeonon.huhuuserservice.constants;

import lombok.Getter;

/**
 * @Author yeonon
 * @date 2019/4/16 0016 15:57
 **/
@Getter
public enum UserRole {
    CUSTOMER(0, "普通用户"),
    VIP(1, "VIP用户"),
    SVIP(2, "SVIP用户"),

    ADMIN(100, "普通管理员"),
    SADMIN(101, "超级管理员");

    private int code;

    private String description;

    UserRole(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static String valueOf(int code) {
        for (UserRole role : values()) {
            if (role.getCode() == code)
                return role.getDescription();
        }
        return null;
    }
}
