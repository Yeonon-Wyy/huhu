package top.yeonon.huhuqaservice.constant;

import lombok.Getter;

/**
 * @Author yeonon
 * @date 2019/4/21 0021 19:43
 **/
@Getter
public enum  CommentType {

    QUESTION(100, "对问题的评论"),
    ANSWER(101, "对答案的评论");

    private int code;

    private String description;

    CommentType(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
