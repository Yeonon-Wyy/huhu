package top.yeonon.huhusearchservice.service.impl;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhusearchservice.constant.ErrMessage;
import top.yeonon.huhusearchservice.entity.Answer;
import top.yeonon.huhusearchservice.entity.Question;
import top.yeonon.huhusearchservice.entity.User;
import top.yeonon.huhusearchservice.repository.AnswerRepository;
import top.yeonon.huhusearchservice.repository.QuestionRepository;
import top.yeonon.huhusearchservice.repository.UserRepository;
import top.yeonon.huhusearchservice.service.ISearchService;
import top.yeonon.huhusearchservice.vo.request.GeneralSearchRequestVo;
import top.yeonon.huhusearchservice.vo.response.SearchAnswerResponseVo;
import top.yeonon.huhusearchservice.vo.response.SearchQuestionResponseVo;
import top.yeonon.huhusearchservice.vo.response.SearchUserResponseVo;

import java.util.List;
import java.util.Random;

/**
 * @Author yeonon
 * @date 2019/4/30 0030 17:19
 **/
@Service
public class SearchServiceImpl implements ISearchService {

    private static final Integer SUMMARY_MIN_COUNT = 20;

    private static final Integer SUMMARY_MAX_COUNT = 50;

    private final QuestionRepository questionRepository;

    private final AnswerRepository answerRepository;

    private final UserRepository userRepository;


    @Autowired
    public SearchServiceImpl(QuestionRepository questionRepository,
                             AnswerRepository answerRepository,
                             UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }


    @Override
    public SearchQuestionResponseVo searchQuestion(GeneralSearchRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }
        Sort sort = new Sort(Sort.Direction.DESC, "followerCount");

        Page<Question> questions = questionRepository.findAllByTitleLikeOrContentLike(
                request.getKeyword(),
                request.getKeyword(),
                PageRequest.of(request.getPageNum(), request.getPageSize(), sort)
        );

        List<SearchQuestionResponseVo.QuestionInfo> questionInfoList = Lists.newArrayList();
        questions.forEach(question -> {
            questionInfoList.add(new SearchQuestionResponseVo.QuestionInfo(
                    Long.parseLong(question.getId()),
                    question.getTitle(),
                    constructSummary(question.getContent()),
                    question.getFollowerCount(),
                    question.getAnswerCount(),
                    question.getCommentCount(),
                    question.getStatus(),
                    question.getCreateTime(),
                    question.getUpdateTime()
            ));
        });
        return new SearchQuestionResponseVo(
                questionInfoList,
                request.getPageNum(),
                request.getPageSize(),
                questions.hasNext(),
                questions.hasPrevious(),
                questions.isFirst(),
                questions.isLast()
        );

    }

    @Override
    public SearchAnswerResponseVo searchAnswer(GeneralSearchRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }

        Sort sort = new Sort(Sort.Direction.DESC, "approvalCount");
        Page<Answer> answers = answerRepository.findAllByContentLike(
                request.getKeyword(),
                PageRequest.of(request.getPageNum(), request.getPageSize(), sort)
        );

        List<SearchAnswerResponseVo.AnswerInfo> answerInfoList = Lists.newArrayList();
        answers.forEach(answer -> {
            answerInfoList.add(new SearchAnswerResponseVo.AnswerInfo(
                    Long.parseLong(answer.getId()),
                    answer.getUserId(),
                    constructSummary(answer.getContent()),
                    answer.getStatus(),
                    answer.getApprovalCount(),
                    answer.getCommentCount(),
                    answer.getCreateTime(),
                    answer.getUpdateTime()
            ));
        });

        return new SearchAnswerResponseVo(
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
    public SearchUserResponseVo searchUser(GeneralSearchRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }
        Sort sort = new Sort(Sort.Direction.DESC, "followerCount");

        Page<User> users = userRepository.findAllByUsernameLikeOrProfileLike(
                request.getKeyword(),
                request.getKeyword(),
                PageRequest.of(request.getPageNum(), request.getPageSize(), sort)
        );

        List<SearchUserResponseVo.UserInfo> userInfoList = Lists.newArrayList();
        users.forEach(user -> {
            userInfoList.add(new SearchUserResponseVo.UserInfo(
                    Long.parseLong(user.getId()),
                    user.getUsername(),
                    user.getProfile(),
                    user.getStatus(),
                    user.getAvatar(),
                    user.getSex(),
                    user.getFollowerCount(),
                    user.getFollowingCount(),
                    user.getIndustry(),
                    user.getDegree()
            ));
        });

        return new SearchUserResponseVo(
                userInfoList,
                request.getPageNum(),
                request.getPageSize(),
                users.hasNext(),
                users.hasPrevious(),
                users.isFirst(),
                users.isLast()
        );
    }


    /**
     * 简单的提取前N个字，N是随机值（20-50，不足则按照实际值）
     * @param content 内容
     * @return 结果
     */
    private String constructSummary(String content) {
        Random random = new Random();
        if (content.length() <= SUMMARY_MAX_COUNT) {
            return content;
        }
        int n = random.nextInt((SUMMARY_MAX_COUNT - SUMMARY_MIN_COUNT) + 1) - SUMMARY_MIN_COUNT;
        return content.substring(0, n);
    }
}
