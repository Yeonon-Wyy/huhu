package top.yeonon.huhuqaservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhuqaservice.service.ICommentService;
import top.yeonon.huhuqaservice.utils.QAUtils;
import top.yeonon.huhuqaservice.vo.comment.request.AnswerCommentCreateRequestVo;
import top.yeonon.huhuqaservice.vo.comment.request.AnswerCommentQueryAllRequestVo;
import top.yeonon.huhuqaservice.vo.comment.request.QuestionCommentCreateRequestVo;
import top.yeonon.huhuqaservice.vo.comment.request.QuestionCommentQueryAllRequestVo;
import top.yeonon.huhuqaservice.vo.comment.response.CommentCreateResponseVo;
import top.yeonon.huhuqaservice.vo.comment.response.CommentQueryAllResponseVo;

/**
 * @Author yeonon
 * @date 2019/4/21 0021 20:00
 **/
@RestController
@RequestMapping("/comments")
public class CommentController {

    @Value("${huhu.security.jwt.signKey}")
    private String signKey;

    private final ICommentService commentService;

    @Autowired
    public CommentController(ICommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/question/{questionId}")
    public CommentCreateResponseVo createQuestionComment(@PathVariable("questionId") Long questionId,
                                                         @RequestBody QuestionCommentCreateRequestVo request,
                                                         Authentication authentication) throws HuhuException {
        Long userId = QAUtils.parseUserIdFromAuthentication(authentication, signKey);
        request.setUserId(userId);
        request.setQuestionId(questionId);
        return commentService.createQuestionComment(request);
    }

    @PostMapping("/answer/{answerId}")
    public CommentCreateResponseVo createAnswerComment(@PathVariable("answerId") Long answerId,
                                                       @RequestBody AnswerCommentCreateRequestVo request,
                                                       Authentication authentication) throws HuhuException {
        Long userId = QAUtils.parseUserIdFromAuthentication(authentication, signKey);
        request.setUserId(userId);
        request.setAnswerId(answerId);
        return commentService.createAnswerComment(request);
    }

    @GetMapping("/question/{questionId}")
    public CommentQueryAllResponseVo queryAllQuestionComment(@PathVariable("questionId") Long questionId,
                                                             QuestionCommentQueryAllRequestVo request,
                                                             @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                                                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) throws HuhuException {
        request.setQuestionId(questionId);
        request.setPageNum(pageNum);
        request.setPageSize(pageSize);
        return commentService.queryAllQuestionComment(request);
    }

    @GetMapping("/answer/{answerId}")
    public CommentQueryAllResponseVo queryAllAnswerComment(@PathVariable("answerId") Long answerId,
                                                           AnswerCommentQueryAllRequestVo request,
                                                           @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) throws HuhuException {
        request.setAnswerId(answerId);
        request.setPageNum(pageNum);
        request.setPageSize(pageSize);
        return commentService.queryAllAnswerComment(request);
    }

}
