package top.yeonon.huhuqaservice.service;

import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhuqaservice.vo.comment.request.*;
import top.yeonon.huhuqaservice.vo.comment.response.CommentCreateResponseVo;
import top.yeonon.huhuqaservice.vo.comment.response.CommentQueryAllResponseVo;

/**
 * @Author yeonon
 * @date 2019/4/21 0021 19:18
 **/
public interface ICommentService {

    /**
     * 新建评论（对问题）
     * @param request 请求对象
     * @return 响应对象
     * @throws HuhuException 可能抛出的异常
     */
    CommentCreateResponseVo createQuestionComment(QuestionCommentCreateRequestVo request) throws HuhuException;

    /**
     * 新建评论（对答案）
     * @param request 请求对象
     * @return 响应对象
     * @throws HuhuException 可能抛出的异常
     */
    CommentCreateResponseVo createAnswerComment(AnswerCommentCreateRequestVo request) throws HuhuException;


    /**
     * 查询某个问题的所有评论
     * @param request 请求对象
     * @return 包含分页信息的对象
     * @throws HuhuException 可能抛出的异常
     */
    CommentQueryAllResponseVo queryAllQuestionComment(QuestionCommentQueryAllRequestVo request) throws HuhuException;

    /**
     * 查询某个回答下的所有评论
     * @param request 请求对象
     * @return 包含分页信息的对象
     * @throws HuhuException 可能抛出的异常
     */
    CommentQueryAllResponseVo queryAllAnswerComment(AnswerCommentQueryAllRequestVo request) throws HuhuException;


}
