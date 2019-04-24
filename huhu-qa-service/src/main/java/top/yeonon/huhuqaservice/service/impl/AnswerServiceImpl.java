package top.yeonon.huhuqaservice.service.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhuqaservice.constant.AnswerStatus;
import top.yeonon.huhuqaservice.constant.Const;
import top.yeonon.huhuqaservice.constant.ErrMessage;
import top.yeonon.huhuqaservice.entity.Answer;
import top.yeonon.huhuqaservice.repository.AnswerRepository;
import top.yeonon.huhuqaservice.repository.QuestionRepository;
import top.yeonon.huhuqaservice.service.IAnswerService;
import top.yeonon.huhuqaservice.vo.answer.request.*;
import top.yeonon.huhuqaservice.vo.answer.response.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author yeonon
 * @date 2019/4/20 0020 17:40
 **/
@Service
@Slf4j
public class AnswerServiceImpl implements IAnswerService {

    private final QuestionRepository questionRepository;

    private final AnswerRepository answerRepository;

    private final RedisTemplate<String, String> redisTemplate;


    @Autowired
    public AnswerServiceImpl(QuestionRepository questionRepository,
                             AnswerRepository answerRepository,
                             RedisTemplate<String, String> redisTemplate) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.redisTemplate = redisTemplate;
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
        Sort sort = new Sort(Sort.Direction.DESC, "approvalCount");
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
                request.getPageSize(),
                answers.hasNext(),
                answers.hasPrevious(),
                answers.isFirst(),
                answers.isLast()
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

    @Override
    public AnswerBatchQueryByUserIdResponseVo queryAnswerByUserId(AnswerBatchQueryByUserIdRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }

        Sort sort = new Sort(Sort.Direction.DESC, "approvalCount");
        Page<Answer> answers = answerRepository.findAllByUserId(request.getUserId(),PageRequest.of(
                request.getPageNum(),
                request.getPageSize(),
                sort
        ));

        List<AnswerBatchQueryByUserIdResponseVo.AnswerInfo> answerInfoList = Lists.newArrayList();
        answers.forEach(answer -> {
            answerInfoList.add(new AnswerBatchQueryByUserIdResponseVo.AnswerInfo(
                    answer.getId(),
                    answer.getUserId(),
                    answer.getContent(),
                    answer.getApprovalCount(),
                    answer.getCommentCount(),
                    answer.getQuestionId(),
                    questionRepository.findTitleById(answer.getQuestionId()),
                    answer.getStatus(),
                    answer.getCreateTime(),
                    answer.getUpdateTime()
            ));
        });

        return new AnswerBatchQueryByUserIdResponseVo(
                answerInfoList,
                request.getPageNum(),
                request.getPageSize(),
                answers.hasNext(),
                answers.hasPrevious(),
                answers.isFirst(),
                answers.isLast()
        );
    }


    @Override
    public AnswerApprovalResponseVo approvalAnswer(AnswerApprovalRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }
        Long index = redisTemplate.opsForZSet().rank(
                Const.RedisConst.ANSWER_APPROVAL_KEY + ":user:" + request.getUserId(),
                request.getAnswerId().toString());
        if (index != null) {
            throw new HuhuException(ErrMessage.ALREADY_APPROVAL_ANSWER);
        }
        Double scoreDelta = 1.0;
        //直接调用increment操作即可，如果redis中不存在key或者member都会直接添加
        redisTemplate.opsForZSet().incrementScore(
                Const.RedisConst.ANSWER_APPROVAL_KEY,
                request.getAnswerId().toString(),
                scoreDelta
        );

        //构造ANSWER_APPROVAL_KEY:user:{userId}这样的键，其member为问题的id，score为点赞的时间戳
        //表达的意思即：该用户点赞了哪些回答，并且可以利用时间戳得到最新的点赞信息
        redisTemplate.opsForZSet().add(
                Const.RedisConst.ANSWER_APPROVAL_KEY + ":user:" + request.getUserId(),
                request.getAnswerId().toString(),
                System.currentTimeMillis()
        );

        Double currentApprovalCount = redisTemplate.opsForZSet().score(
                Const.RedisConst.ANSWER_APPROVAL_KEY,
                request.getAnswerId().toString()
        );
        if (currentApprovalCount == null) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }
        return new AnswerApprovalResponseVo(
                request.getAnswerId(),
                currentApprovalCount.longValue()
        );

        //该方法不开启事务，也不持久化到关系型数据库里
    }

    @Override
    @Transactional
    public void updateAnswerApprovalCount() {
        Set<ZSetOperations.TypedTuple<String>> items = redisTemplate.opsForZSet().rangeWithScores(
                Const.RedisConst.ANSWER_APPROVAL_KEY,
                0,
                -1
        );
        if (items == null) {
            return;
        }
        for (ZSetOperations.TypedTuple<String> item : items) {
            Long id = Long.parseLong(item.getValue());
            Long approvalCount = item.getScore().longValue();
            answerRepository.updateApprovalCount(approvalCount, id);
        }
    }
}
