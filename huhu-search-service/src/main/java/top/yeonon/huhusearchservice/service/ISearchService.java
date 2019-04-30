package top.yeonon.huhusearchservice.service;

import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhusearchservice.vo.request.GeneralSearchRequestVo;
import top.yeonon.huhusearchservice.vo.response.SearchAnswerResponseVo;
import top.yeonon.huhusearchservice.vo.response.SearchQuestionResponseVo;
import top.yeonon.huhusearchservice.vo.response.SearchUserResponseVo;

/**
 * @Author yeonon
 * @date 2019/4/30 0030 17:19
 **/
public interface ISearchService {


    /**
     * 搜索问题
     * @param request 请求对象，包含了关键字
     * @return 响应对象，仅仅返回必要的信息
     * @throws HuhuException 可能抛出的异常
     */
    SearchQuestionResponseVo searchQuestion(GeneralSearchRequestVo request)
        throws HuhuException;


    /**
     * 搜索回答
     * @param request 请求对象，包含关键字
     * @return 响应对象
     * @throws HuhuException 可能抛出的异常
     */
    SearchAnswerResponseVo searchAnswer(GeneralSearchRequestVo request)
        throws HuhuException;

    /**
     * 搜索用户
     * @param request 请求对象，包含关键字
     * @return 响应对象
     * @throws HuhuException 可能抛出的异常
     */
    SearchUserResponseVo searchUser(GeneralSearchRequestVo request)
        throws HuhuException;
}
