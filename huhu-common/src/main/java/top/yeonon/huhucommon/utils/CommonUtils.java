package top.yeonon.huhucommon.utils;

import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Random;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 15:42
 **/
public class CommonUtils {

    public static String md5(String originStr) {
        return DigestUtils.md5DigestAsHex(originStr.getBytes());
    }


    /**
     * 随机生成简单的x位验证码
     * @param x 验证码位数
     * @return 随机验证码
     */
    public static String generateValidateCode(int x) {
        final char[] chars = "1234567890QWERTYUIOPASDFGHJKLZXCVBNM".toCharArray();
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        int index;   //获取随机chars下标
        for (int i = 0; i < x; i++) {
            index = random.nextInt(chars.length);  //获取随机chars下标
            buffer.append(chars[index]);
        }
        return buffer.toString();
    }
}
