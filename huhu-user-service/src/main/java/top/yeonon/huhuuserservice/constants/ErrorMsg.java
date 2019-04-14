package top.yeonon.huhuuserservice.constants;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 15:39
 **/
public class ErrorMsg {

    public static final String REQUEST_PARAM_ERROR = "请求参数错误";

    public static final String EXIST_SAME_USERNAME = "存在同名用户";

    public static final String NOT_FOUND_USER = "找不到该用户";

    public static final String ALREADY_CLOSE_USER = "该用户已经注销";

    public static final String NOT_ALLOW_FOLLOW_YOURSELF = "不允许关注自己";

    public static final String NOT_ALLOW_UNFOLLOW_YOURSELF = "不允许取消关注自己";

    public static final String NOT_EXIST_FOLLOW_RELATION = "双方不存在任何关注关系";

    public static final String EXIST_FOLLOW_RELATION = "双方已存在关注关系";
}
