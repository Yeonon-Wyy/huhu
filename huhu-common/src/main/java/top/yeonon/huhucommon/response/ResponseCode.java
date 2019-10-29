package top.yeonon.huhucommon.response;

import lombok.Getter;

/**
 * @Author yeonon
 * @date 2019/4/9 0009 19:33
 **/
@Getter
public enum ResponseCode {

    //0 mean success code
    SUCCESS(0, "response success"),
    //1 ~ 99 mean common error code
    REQUEST_PARAM_ERROR(1, "request parameter error, please recheck it"),
    NOT_ALLOW_ACTION(2, "the action was not allowed"),
    VALIDATE_CODE_ERROR(3, "validate code error"),
    NOT_EXIST_TOKEN_IN_HEADER(4, "not exist token in header"),
    UPLOAD_FILE_ERROR(5, "upload file error"),
    JSON_PARSE_ERROR(6, "JSON parse error"),
    SERVICE_ERROR(99, "internal service error"),


    //500 ~ 599 mean QA service error code
    EXIST_SAME_TITLE(500, "exist same question title, please recheck it"),
    NOT_FOUND_QUESTION(501, "can not found this question, please recheck your parameter"),
    NOT_FOUND_ANSWER(502, "can not found this answer, please recheck your parameter"),
    EXIST_QUESTION_FOLLOWER(503, "the user already follow this question"),
    NOT_EXIST_QUESTION_FOLLOWER(504, "the user didn't follow this question"),
    EXIST_SAME_QUESTION_ANSWER(506, "the user already answer this question"),
    EXIST_SAME_QUESTION_COMMENT(507, "the user already comment this question"),
    EXIST_SAME_ANSWER_COMMENT(508, "the user already comment this answer"),
    ALREADY_APPROVAL_ANSWER(509, "the user already approval this answer"),




    //600 ~ 699 mean search service error code
    MYSQL_URL_PARSE__ERROR(600, "mysql url parse error"),
    DATA_PARSE_ERROR(601, "data parse error"),

    //700 ~ 799 mean user service error code
    EXIST_SAME_USERNAME(700, "exist same username in our database"),
    NOT_FOUND_USER(701, "can not found this user"),
    ALREADY_CLOSE_USER(702, "the user already close"),
    NOT_ALLOW_FOLLOW_YOURSELF(703, "can not follow yourself"),
    NOT_ALLOW_UN_FOLLOW_YOURSELF(704, "can not cancel follow yourself"),
    NOT_EXIST_FOLLOW_RELATION(705, "there is no relationship of follow"),
    EXIST_FOLLOW_RELATION(706,"there is relationship of follow"),
    USERNAME_NOT_MATCH_EMAIL(707, "the username can not match email of given"),

    //800 ~ 899 mean auth service error code
    INVALID_USERNAME(800, "invalid username"),
    USERNAME_OR_PASSWORD_ERROR(801, "username or password error"),
    INVALID_TOKEN(802, "invalid token"),

    //900 ~ 999 mean error code of mail service
    INVALID_FORMAT(900, "invalid email format, please recheck it"),


    ;

    private int code;

    private String description;

    ResponseCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
