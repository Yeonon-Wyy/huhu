package top.yeonon.huhucommon.utils;

import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 15:42
 **/
public class CommonUtils {

    public static String md5(String originStr) {
        return DigestUtils.md5DigestAsHex(originStr.getBytes());
    }
}
