package top.yeonon.huhuqaservice.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhucommon.utils.CommonUtils;
import top.yeonon.huhuqaservice.constant.ErrMessage;
import top.yeonon.huhuqaservice.interceptor.annatation.CheckUserId;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author yeonon
 * @date 2019/4/20 0020 13:40
 **/
@Component
public class CheckUserIdInterceptor implements HandlerInterceptor {

    String AUTH_TOKEN = "Authorization";

    @Value("${huhu.security.jwt.signKey}")
    private String singKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        //先判断是否需要检查ID
        if (!handlerMethod.getMethod().isAnnotationPresent(CheckUserId.class)) {
            return true;
        }

        //从请求头中获取Token（需要检查ID的说明肯定需要用户认证，即header里的Token肯定不会为空）
        String token = request.getHeader(AUTH_TOKEN).substring(7);
        Claims body = CommonUtils.parseJwtToken(token, singKey);

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
        return Long.valueOf(String.valueOf(info.get("userId")));
    }

}
