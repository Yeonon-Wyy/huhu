package top.yeonon.huhucommon.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.yeonon.huhucommon.response.ResponseCode;
import top.yeonon.huhucommon.response.ServerResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author yeonon
 * @date 2019/4/9 0009 19:45
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = HuhuException.class)
    public ServerResponse huhuExceptionHandler(HttpServletRequest request, HuhuException e) {
        String message = String.format("request uri %s and method %s occur an error : %s",
                request.getRequestURI(),
                request.getMethod(),
                e.getMessage());

        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setCode(e.getCode());
        serverResponse.setMessage(message);
        return serverResponse;
    }
}
