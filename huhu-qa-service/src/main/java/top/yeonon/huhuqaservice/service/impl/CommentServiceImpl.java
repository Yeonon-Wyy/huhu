package top.yeonon.huhuqaservice.service.impl;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.yeonon.huhucommon.aop.ParamValidate;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhucommon.response.ResponseCode;
import top.yeonon.huhuqaservice.constant.CommentType;
import top.yeonon.huhuqaservice.entity.AnswerComment;
import top.yeonon.huhuqaservice.entity.Comment;
import top.yeonon.huhuqaservice.entity.QuestionComment;
import top.yeonon.huhuqaservice.repository.*;
import top.yeonon.huhuqaservice.service.ICommentService;
import top.yeonon.huhuqaservice.vo.comment.request.AnswerCommentCreateRequestVo;
import top.yeonon.huhuqaservice.vo.comment.request.AnswerCommentQueryAllRequestVo;
import top.yeonon.huhuqaservice.vo.comment.request.QuestionCommentCreateRequestVo;
import top.yeonon.huhuqaservice.vo.comment.request.QuestionCommentQueryAllRequestVo;
import top.yeonon.huhuqaservice.vo.comment.response.CommentCreateResponseVo;
import top.yeonon.huhuqaservice.vo.comment.response.CommentQueryAllResponseVo;

import java.util.List;

/**
 * @Author yeonon
 * @date 2019/4/21 0021 19:19
 **/
@Service
public class CommentServiceImpl implements ICommentService {

    private final CommentRepository commentRepository;

    private final QuestionCommentRepository questionCommentRepository;

    private final QuestionRepository questionRepository;

    private final AnswerCommentRepository answerCommentRepository;

    private final AnswerRepository answerRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
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
    @ParamValidate
    @Transactional
    public CommentCreateResponseVo createQuestionComment(QuestionCommentCreateRequestVo request) throws HuhuException {

        if (questionCommentRepository.existsByUserIdAndQuestionId(request.getUserId(), request.getQuestionId())) {
            throw new HuhuException(ResponseCode.EXIST_SAME_QUESTION_COMMENT.getCode(),
                    ResponseCode.EXIST_SAME_QUESTION_COMMENT.getDescription());
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
    @ParamValidate
    @Transactional
    public CommentCreateResponseVo createAnswerComment(AnswerCommentCreateRequestVo request) throws HuhuException {


        if (answerCommentRepository.existsByUserIdAndAnswerId(request.getUserId(), request.getAnswerId())) {
            throw new HuhuException(ResponseCode.EXIST_SAME_ANSWER_COMMENT.getCode(),
                    ResponseCode.EXIST_SAME_ANSWER_COMMENT.getDescription());
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

    @Override
    @ParamValidate
    public CommentQueryAllResponseVo queryAllQuestionComment(QuestionCommentQueryAllRequestVo request) throws HuhuException {

        if (!questionRepository.existsById(request.getQuestionId())) {
            throw new HuhuException(ResponseCode.NOT_FOUND_QUESTION.getCode(),
                    ResponseCode.NOT_FOUND_QUESTION.getDescription());
        }


        List<Long> commentIdList = questionCommentRepository.findCommentIdByQuestionId(request.getQuestionId());

        Sort sort = new Sort(Sort.Direction.DESC, "approvalCount");
        Page<Comment> commentList = commentRepository.findAllByIdIn(commentIdList, PageRequest.of(
                request.getPageNum(),
                request.getPageSize(),
                sort
        ));
        List<CommentQueryAllResponseVo.CommentInfo> commentInfoList = Lists.newArrayList();
        commentList.forEach(comment -> {
            commentInfoList.add(new CommentQueryAllResponseVo.CommentInfo(
                    comment.getId(),
                    comment.getUserId(),
                    comment.getStatus(),
                    comment.getContent(),
                    comment.getType(),
                    comment.getApprovalCount(),
                    comment.getCreateTime(),
                    comment.getUpdateTime()
            ));
        });

        return new CommentQueryAllResponseVo(
                commentInfoList,
                request.getPageNum(),
                request.getPageSize(),
                commentList.hasNext(),
                commentList.hasPrevious(),
                commentList.isFirst(),
                commentList.isLast()
        );


    }

    @Override
    @ParamValidate
    public CommentQueryAllResponseVo queryAllAnswerComment(AnswerCommentQueryAllRequestVo request) throws HuhuException {


        if (!answerRepository.existsById(request.getAnswerId())) {
            throw new HuhuException(ResponseCode.NOT_FOUND_ANSWER.getCode(),
                    ResponseCode.NOT_FOUND_ANSWER.getDescription());
        }

        List<Long> commentIdList = answerCommentRepository.findCommentIdByAnswerId(request.getAnswerId());
        Sort sort = new Sort(Sort.Direction.DESC, "approvalCount");
        Page<Comment> commentList = commentRepository.findAllByIdIn(commentIdList, PageRequest.of(
                request.getPageNum(),
                request.getPageSize(),
                sort
        ));

        List<CommentQueryAllResponseVo.CommentInfo> commentInfoList = Lists.newArrayList();
        commentList.forEach(comment -> {
            commentInfoList.add(new CommentQueryAllResponseVo.CommentInfo(
                    comment.getId(),
                    comment.getUserId(),
                    comment.getStatus(),
                    comment.getContent(),
                    comment.getType(),
                    comment.getApprovalCount(),
                    comment.getCreateTime(),
                    comment.getUpdateTime()
            ));
        });

        return new CommentQueryAllResponseVo(
                commentInfoList,
                request.getPageNum(),
                request.getPageSize(),
                commentList.hasNext(),
                commentList.hasPrevious(),
                commentList.isFirst(),
                commentList.isLast()
        );

    }


}
