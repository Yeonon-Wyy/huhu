package top.yeonon.huhuqaservice.utils;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import top.yeonon.huhucommon.utils.CommonUtils;

/**
 * @Author yeonon
 * @date 2019/4/20 0020 18:04
 **/
public class QAUtils {

    /**
     * 从authentication对象中获取用户id信息（其实主要是从Jwt中获取）
     * @param authentication authentication对象，携带了当前用户的各种信息
     * @param signKey jwt签名
     * @return 响应对象
     */
    public static Long parseUserIdFromAuthentication(Authentication authentication, String signKey) {
        String token = ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
        Claims body = CommonUtils.parseJwtToken(token, signKey);
        return Long.parseLong(String.valueOf(body.get("id")));
    }
}
