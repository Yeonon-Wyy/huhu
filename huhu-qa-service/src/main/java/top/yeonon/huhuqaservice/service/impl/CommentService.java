package top.yeonon.huhuqaservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhuqaservice.constant.CommentType;
import top.yeonon.huhuqaservice.constant.ErrMessage;
import top.yeonon.huhuqaservice.entity.AnswerComment;
import top.yeonon.huhuqaservice.entity.Comment;
import top.yeonon.huhuqaservice.entity.QuestionComment;
import top.yeonon.huhuqaservice.repository.*;
import top.yeonon.huhuqaservice.service.ICommentService;
import top.yeonon.huhuqaservice.vo.comment.request.AnswerCommentCreateRequestVo;
import top.yeonon.huhuqaservice.vo.comment.request.QuestionCommentCreateRequestVo;
import top.yeonon.huhuqaservice.vo.comment.response.CommentCreateResponseVo;

/**
 * @Author yeonon
 * @date 2019/4/21 0021 19:19
 **/
@Service
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;

    private final QuestionCommentRepository questionCommentRepository;

    private final QuestionRepository questionRepository;

    private final AnswerCommentRepository answerCommentRepository;

    private final AnswerRepository answerRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository,
                          QuestionCommentRepository questionCommentRepository,
                          QuestionRepository questionRepository,
                          AnswerCommentRepository answerCommentRepository,
                          AnswerRepository answerRepository) {
        this.commentRepository = commentRepository;
        this.questionCommentRepository = questionCommentRepository;
        this.questionRepository = questionRepository;
        this.answerCommentRepository = answerCommentRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    @Transactional
    public CommentCreateResponseVo createQuestionComment(QuestionCommentCreateRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }

        if (questionCommentRepository.existsByUserIdAndQuestionId(request.getUserId(), request.getQuestionId())) {
            throw new HuhuException(ErrMessage.EXIST_SAME_QUESTION_COMMENT);
        }

        Comment comment = new Comment(
                request.getUserId(),
                CommentType.QUESTION.getCode(),
                request.getContent()
        );

        //入库
        comment = commentRepository.save(comment);

        //修改问题的评论数
        questionRepository.incrementCommentCountById(request.getQuestionId());

        //存入关联表
        questionCommentRepository.save(new QuestionComment(
                request.getQuestionId(),
                comment.getId(),
                request.getUserId()
        ));

        return new CommentCreateResponseVo(comment.getId());
    }

    @Override
    @Transactional
    public CommentCreateResponseVo createAnswerComment(AnswerCommentCreateRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }

        if (answerCommentRepository.existsByUserIdAndAnswerId(request.getUserId(), request.getAnswerId())) {
            throw new HuhuException(ErrMessage.EXIST_SAME_ANSWER_COMMENT);
        }

        Comment comment = new Comment(
                request.getUserId(),
                CommentType.ANSWER.getCode(),
                request.getContent()
        );

        //入库
        comment = commentRepository.save(comment);

        //存入关联表
        answerCommentRepository.save(new AnswerComment(
                request.getAnswerId(),
                comment.getId(),
                request.getUserId()
        ));

        //增加answer中commentCount
        answerRepository.incrementCommentCountById(request.getAnswerId());
        return new CommentCreateResponseVo(comment.getId());
    }


}
