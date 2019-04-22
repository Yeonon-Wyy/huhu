package top.yeonon.huhucommon.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.Base64Codec;
import org.springframework.util.DigestUtils;

import java.util.Arrays;
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
        //获取随机chars下标
        int index;
        for (int i = 0; i < x; i++) {
            //获取随机chars下标
            index = random.nextInt(chars.length);
            buffer.append(chars[index]);
        }
        return buffer.toString();
    }

    public static Claims parseJwtToken(String jwtToken, String sign) {
        return Jwts.parser()
                .setSigningKey(Base64Codec.BASE64.encode(sign))
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    public static boolean checkId(Long ...ids) {
        return Arrays.stream(ids)
                .allMatch(id -> id != null && id > 0);
    }
}
