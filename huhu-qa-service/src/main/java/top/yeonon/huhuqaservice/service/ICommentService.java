package top.yeonon.huhuqaservice.service;

import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhuqaservice.vo.comment.request.AnswerCommentCreateRequestVo;
import top.yeonon.huhuqaservice.vo.comment.request.QuestionCommentCreateRequestVo;
import top.yeonon.huhuqaservice.vo.comment.response.CommentCreateResponseVo;

/**
 * @Author yeonon
 * @date 2019/4/21 0021 19:18
 **/
public interface ICommentService {

    /**
     * 新建评论（对问题）
     * @param request 请求对象
     * @return 响应对象
     */
    CommentCreateResponseVo createQuestionComment(QuestionCommentCreateRequestVo request) throws HuhuException;

    /**
     * 新建评论（对答案）
     * @param request 请求对象
     * @return 响应对象
     */
    CommentCreateResponseVo createAnswerComment(AnswerCommentCreateRequestVo request) throws HuhuException;
}
