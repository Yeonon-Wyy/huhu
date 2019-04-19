package top.yeonon.huhuqaservice.service;

import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhuqaservice.vo.request.QuestionCreateRequestVo;
import top.yeonon.huhuqaservice.vo.response.QuestionCreateResponseVo;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 18:59
 **/
public interface IQuestionService {

    /**
     * 创建一个问题
     * @param request 请求对象
     * @return 响应对象
     */
    QuestionCreateResponseVo createQuestion(QuestionCreateRequestVo request) throws HuhuException;
}
