package top.yeonon.huhucommon.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import top.yeonon.huhucommon.constant.CommonErrorMessage;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhucommon.request.RequestVo;

/**
 * @author: yeonon
 * @date: 2019/10/28
 */
@Aspect
@Component
public class ParamValidateAop {

    @Pointcut("@annotation(ParamValidate)")
    public void validateParam() {

    }

    @Before("validateParam()")
    public void doBefore(JoinPoint joinpoint) throws HuhuException {
        for (Object arg : joinpoint.getArgs()) {
            if (arg instanceof RequestVo) {
                RequestVo requestVo = (RequestVo)arg;
                    if (!requestVo.validate()) {
                    throw new HuhuException(CommonErrorMessage.REQUEST_PARAM_ERROR);
                }
            }
        }
    }
}
