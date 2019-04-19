package top.yeonon.huhuqaservice.constant;

import lombok.Getter;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 18:42
 **/
@Getter
public enum  TagStatus {

    NORMAL(0, "正常"),
    INVALID(200, "无效");

    private int code;

    private String description;

    TagStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

}
