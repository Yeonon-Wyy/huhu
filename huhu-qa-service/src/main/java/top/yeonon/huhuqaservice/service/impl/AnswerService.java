package top.yeonon.huhuqaservice.service.impl;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhuqaservice.constant.AnswerStatus;
import top.yeonon.huhuqaservice.constant.ErrMessage;
import top.yeonon.huhuqaservice.entity.Answer;
import top.yeonon.huhuqaservice.repository.AnswerRepository;
import top.yeonon.huhuqaservice.repository.QuestionRepository;
import top.yeonon.huhuqaservice.service.IAnswerService;
import top.yeonon.huhuqaservice.vo.answer.request.AnswerBatchQueryRequestVo;
import top.yeonon.huhuqaservice.vo.answer.request.AnswerCreateRequestVo;
import top.yeonon.huhuqaservice.vo.answer.request.AnswerDeleteRequestVo;
import top.yeonon.huhuqaservice.vo.answer.request.AnswerUpdateRequestVo;
import top.yeonon.huhuqaservice.vo.answer.response.AnswerBatchQueryResponseVo;
import top.yeonon.huhuqaservice.vo.answer.response.AnswerCreateResponseVo;
import top.yeonon.huhuqaservice.vo.answer.response.AnswerDeleteResponseVo;
import top.yeonon.huhuqaservice.vo.answer.response.AnswerUpdateResponseVo;

import java.util.List;

/**
 * @Author yeonon
 * @date 2019/4/20 0020 17:40
 **/
@Service
public class AnswerService implements IAnswerService {

    private final QuestionRepository questionRepository;

    private final AnswerRepository answerRepository;


    @Autowired
    public AnswerService(QuestionRepository questionRepository,
                         AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    @Transactional
    public AnswerCreateResponseVo createAnswer(AnswerCreateRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }

        if (!questionRepository.existsById(request.getQuestionId())) {
            throw new HuhuException(ErrMessage.NOT_FOUND_QUESTION);
        }

        Answer oldAnswer = answerRepository.findByUserIdAndQuestionId(request.getUserId(), request.getQuestionId());
        if (oldAnswer != null) {
            throw new HuhuException(ErrMessage.EXIST_SAME_QUESTION_ANSWER);
        }

        Answer answer = new Answer(
                request.getUserId(),
                request.getQuestionId(),
                request.getContent()
        );
        //存入数据库
        answer = answerRepository.save(answer);
        questionRepository.incrementAnswerCountById(request.getQuestionId());

        return new AnswerCreateResponseVo(answer.getId());
    }

    @Override
    public AnswerBatchQueryResponseVo batchQueryAnswer(AnswerBatchQueryRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }

        //分页查询数据
        Sort sort = new Sort(Sort.Direction.ASC, "approvalCount");
        Page<Answer> answers = answerRepository.findAllByQuestionId(
                request.getQuestionId(),
                PageRequest.of(
                request.getPageNum(),
                request.getPageSize(),
                sort)
        );

        //组装VO对象
        List<AnswerBatchQueryResponseVo.AnswerInfo> answerInfoList = Lists.newArrayList();
        answers.forEach(answer -> {
            answerInfoList.add(new AnswerBatchQueryResponseVo.AnswerInfo(
                    answer.getId(),
                    answer.getUserId(),
                    answer.getContent(),
                    answer.getStatus(),
                    answer.getApprovalCount(),
                    answer.getCommentCount(),
                    answer.getCreateTime(),
                    answer.getUpdateTime()
            ));
        });

        return new AnswerBatchQueryResponseVo(
                answerInfoList,
                request.getPageNum(),
                request.getPageSize()
        );

    }

    @Override
    @Transactional
    public AnswerUpdateResponseVo updateAnswer(AnswerUpdateRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }

        Answer answer = answerRepository.findByIdAndUserId(request.getId(), request.getUserId());
        if (answer == null
                || answer.getStatus() >= AnswerStatus.CLOSE.getCode()) {
            throw new HuhuException(ErrMessage.NOT_ALLOW_ACTION);
        }

        answer = request.update(answer);
        answerRepository.save(answer);

        return new AnswerUpdateResponseVo(answer.getId());
    }

    @Override
    @Transactional
    public AnswerDeleteResponseVo deleteAnswer(AnswerDeleteRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }

        Answer answer = answerRepository.findByIdAndUserId(request.getId(), request.getUserId());
        if (answer == null
                || answer.getStatus() == AnswerStatus.CLOSE.getCode()) {
            throw new HuhuException(ErrMessage.NOT_ALLOW_ACTION);
        }

        answer.setStatus(AnswerStatus.CLOSE.getCode());
        return new AnswerDeleteResponseVo(answer.getId());
    }


}
