package top.yeonon.huhucommon.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.yeonon.huhucommon.advice.annotation.IgnoreAdvice;
import top.yeonon.huhucommon.response.ResponseCode;
import top.yeonon.huhucommon.response.ServerResponse;


/**
 * @Author yeonon
 * @date 2019/4/9 0009 19:35
 **/
@RestControllerAdvice
public class ControllerAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (returnType.getDeclaringClass().isAnnotationPresent(IgnoreAdvice.class)) {
            return false;
        }
        if (returnType.getMethod().isAnnotationPresent(IgnoreAdvice.class)) {
            return false;
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setCode(ResponseCode.SUCCESS.getCode());
        serverResponse.setMessage(ResponseCode.SUCCESS.getDescription());

        if (body == null) {
            return serverResponse;
        }
        if (body instanceof ServerResponse) {
            return body;
        }
        serverResponse.setData(body);

        return serverResponse;
    }
}
