package top.yeonon.huhuuserservice.constants;

/**
 * @Author yeonon
 * @date 2019/4/16 0016 18:07
 **/
public class Const {

    public interface RedisConst {

        String FORGET_PASSWORD_VALIDATE_CODE_PREFIX = "forget_password:validate_code:";

        Long FORGET_PASSWORD_VALIDATE_CODE_TIMEOUT = 300L; //5分钟

        Integer FORGET_PASSWORD_VALIDATE_CODE_COUNT = 6;
    }

    public interface UserConst {
        String AVATAR_DIR = "avatar/";
    }

}
