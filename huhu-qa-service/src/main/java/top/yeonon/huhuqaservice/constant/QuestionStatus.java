package top.yeonon.huhuqaservice.constant;

import lombok.Getter;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 17:53
 **/
@Getter
public enum QuestionStatus {
    NORMAL(0, "正常"),
    CLOSE(100, "关闭"),
    INVALID(200, "违规");

    private int code;

    private String description;

    QuestionStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
