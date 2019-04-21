package top.yeonon.huhuauthserver.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author yeonon
 * @date 2019/4/18 0018 20:41
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtProperties {

    private static final Integer DEFAULT_ACCESS_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60; //7 days

    private static final Integer DEFAULT_REFRESH_TOKEN_EXPIRE_TIME = 30 * 24 * 60 * 60; //30 days

    private String signKey;

    //有效时间
    private Integer accessTokenExpireTime = DEFAULT_ACCESS_TOKEN_EXPIRE_TIME;

    private Integer refreshTokenExpireTime = DEFAULT_REFRESH_TOKEN_EXPIRE_TIME;


}
