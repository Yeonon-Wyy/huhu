package top.yeonon.huhuuserservice.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhucommon.utils.CommonUtils;
import top.yeonon.huhuuserservice.constants.ErrMessage;
import top.yeonon.huhuuserservice.interceptor.annotation.CheckId;
import top.yeonon.huhuuserservice.properties.HuhuSecurityProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 13:43
 **/
@Component
public class CheckIdInterceptor implements HandlerInterceptor {

    String AUTH_TOKEN = "Authorization";


    private final HuhuSecurityProperties huhuSecurityProperties;

    @Autowired
    public CheckIdInterceptor(HuhuSecurityProperties huhuSecurityProperties) {
        this.huhuSecurityProperties = huhuSecurityProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        //先判断是否需要检查ID
        if (!handlerMethod.getMethod().isAnnotationPresent(CheckId.class)) {
            return true;
        }

        //从请求头中获取Token
        String token = request.getHeader(AUTH_TOKEN).substring(7);
        Claims body = CommonUtils.parseJwtToken(token, huhuSecurityProperties.getJwt().getSignKey());

        //获取到JWT中的ID
        Long tokenWithId = Long.parseLong(String.valueOf(body.get("id")));
        Long id = getId(request);

        //与传入的ID做比较，如果不相等，则抛出异常，表示横向越权操作
        if (!id.equals(tokenWithId)) {
            throw new HuhuException(ErrMessage.NOT_ALLOW_ACTION);
        }

        return true;
    }

    private Long getId(HttpServletRequest request) {
        //HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE 这个常量是用于获取REST风格的路径参数，如{id}
        Map info = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return Long.valueOf(String.valueOf(info.get("id")));
    }


}
