package top.yeonon.huhuauthserver.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import top.yeonon.huhucommon.advice.annotation.IgnoreAdvice;
import top.yeonon.huhucommon.exception.HuhuException;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @Author yeonon
 * @date 2019/4/15 0015 17:13
 **/
@RestController
@RequestMapping("/oath/users")
public class OAuthController {

    private static final String AUTH_HEADER = "Authorization";


    private final ConsumerTokenServices consumerTokenServices;

    @Autowired
    public OAuthController(ConsumerTokenServices consumerTokenServices) {
        this.consumerTokenServices = consumerTokenServices;
    }

    @GetMapping("/current")
    @IgnoreAdvice
    public Principal getUser(Principal principal) {
        return principal;
    }


    @DeleteMapping("/logout")
    public void logout(HttpServletRequest request) throws HuhuException {
        String accessTokenWithBearer = request.getHeader(AUTH_HEADER);
        if (StringUtils.isEmpty(accessTokenWithBearer)) {
            throw new HuhuException("无效的token");
        }
        //Bearer加上一个空格一共7个字符
        consumerTokenServices.revokeToken(accessTokenWithBearer.substring(7));
    }



}
