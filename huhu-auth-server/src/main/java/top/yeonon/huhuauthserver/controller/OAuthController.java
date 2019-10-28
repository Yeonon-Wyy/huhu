package top.yeonon.huhuauthserver.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yeonon.huhuauthserver.constant.Const;
import top.yeonon.huhuauthserver.constant.ErrMessage;
import top.yeonon.huhucommon.advice.annotation.IgnoreAdvice;
import top.yeonon.huhucommon.exception.HuhuException;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @Author yeonon
 * @date 2019/4/15 0015 17:13
 **/
@RestController
@RequestMapping("/oauth/users")
public class OAuthController {

    private static final String AUTH_HEADER = Const.AUTH_TOKEN_HEADER_KEY_NAME;


    private final ConsumerTokenServices consumerTokenServices;

    @Autowired
    public OAuthController(ConsumerTokenServices consumerTokenServices) {
        this.consumerTokenServices = consumerTokenServices;
    }

    @GetMapping("/current")
    @IgnoreAdvice
    public Principal getUser(Principal principal) {
        System.out.println("验证用户");
        return principal;
    }


    @DeleteMapping("/logout")
    public void logout(HttpServletRequest request) throws HuhuException {
        String accessTokenWithBearer = request.getHeader(AUTH_HEADER);
        if (StringUtils.isEmpty(accessTokenWithBearer)) {
            throw new HuhuException(ErrMessage.INVALID_TOKEN);
        }
        //Bearer加上一个空格一共7个字符
        consumerTokenServices.revokeToken(accessTokenWithBearer.substring(7));
    }
}
