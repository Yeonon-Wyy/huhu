package top.yeonon.huhusearchservice.service;

import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhusearchservice.vo.SearchQuestionRequestVo;
import top.yeonon.huhusearchservice.vo.SearchQuestionResponseVo;

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
    SearchQuestionResponseVo searchQuestion(SearchQuestionRequestVo request)
        throws HuhuException;

}
