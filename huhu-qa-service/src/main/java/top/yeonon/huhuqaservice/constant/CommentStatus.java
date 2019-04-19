package top.yeonon.huhuqaservice.constant;

import lombok.Getter;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 18:33
 **/
@Getter
public enum  CommentStatus {

    NORMAL(0, "正常"),
    INVALID(200, "违规");

    private int code;

    private String description;

    CommentStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

}
