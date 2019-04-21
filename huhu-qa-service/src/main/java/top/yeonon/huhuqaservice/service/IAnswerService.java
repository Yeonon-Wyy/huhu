package top.yeonon.huhuqaservice.service;

import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhuqaservice.vo.answer.request.*;
import top.yeonon.huhuqaservice.vo.answer.response.*;

/**
 * @Author yeonon
 * @date 2019/4/20 0020 17:05
 **/
public interface IAnswerService {

    /**
     * 新建答案
     * @param request 请求
     * @return 响应
     */
    AnswerCreateResponseVo createAnswer(AnswerCreateRequestVo request) throws HuhuException;

    /**
     * 批量分页获取回答
     * @param request 请求
     * @return 响应
     * @throws HuhuException
     */
    AnswerBatchQueryResponseVo batchQueryAnswer(AnswerBatchQueryRequestVo request) throws HuhuException;

    /**
     * 更新答案内容
     * @param request 请求
     * @return 响应
     */
    AnswerUpdateResponseVo updateAnswer(AnswerUpdateRequestVo request) throws HuhuException;


    /**
     * 删除答案（其实只是关闭答案）
     * @param request 请求
     * @return 响应
     */
    AnswerDeleteResponseVo deleteAnswer(AnswerDeleteRequestVo request) throws HuhuException;


    /**
     * 根据用户ID来查询
     * @param request 请求对象
     * @return 响应
     */
    AnswerBatchQueryByUserIdResponseVo queryAnswerByUserId(AnswerBatchQueryByUserIdRequestVo request) throws HuhuException;
}
