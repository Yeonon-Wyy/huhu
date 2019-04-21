package top.yeonon.huhuqaservice.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhuqaservice.constant.ErrMessage;
import top.yeonon.huhuqaservice.constant.QuestionStatus;
import top.yeonon.huhuqaservice.entity.Question;
import top.yeonon.huhuqaservice.entity.QuestionFollower;
import top.yeonon.huhuqaservice.entity.QuestionTag;
import top.yeonon.huhuqaservice.entity.Tag;
import top.yeonon.huhuqaservice.repository.QuestionFollowerRepository;
import top.yeonon.huhuqaservice.repository.QuestionRepository;
import top.yeonon.huhuqaservice.repository.QuestionTagRepository;
import top.yeonon.huhuqaservice.repository.TagRepository;
import top.yeonon.huhuqaservice.service.IQuestionService;
import top.yeonon.huhuqaservice.vo.question.request.*;
import top.yeonon.huhuqaservice.vo.question.response.*;

import java.util.List;
import java.util.Set;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 19:03
 **/
@Service
public class QuestionService implements IQuestionService {

    private final QuestionRepository questionRepository;

    private final QuestionTagRepository questionTagRepository;

    private final TagRepository tagRepository;

    private final QuestionFollowerRepository questionFollowerRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository,
                           QuestionTagRepository questionTagRepository, TagRepository tagRepository, QuestionFollowerRepository questionFollowerRepository) {
        this.questionRepository = questionRepository;
        this.questionTagRepository = questionTagRepository;
        this.tagRepository = tagRepository;
        this.questionFollowerRepository = questionFollowerRepository;
    }

    @Override
    @Transactional
    public QuestionCreateResponseVo createQuestion(QuestionCreateRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }

        if (questionRepository.existsByTitle(request.getTitle())) {
            throw new HuhuException(ErrMessage.EXIST_SAME_TITLE);
        }

        //插入数据
        Question question = new Question(
                request.getTitle(),
                request.getContent(),
                request.getUserId()
        );

        question = questionRepository.save(question);
        Long questionId = question.getId();
        List<QuestionTag> questionTagList = Lists.newArrayList();

        //先检查tag表中的数据
        Set<Long> tagIds = checkAndFilterTags(request.getTagNames());

        //插入到question->tag关联表中
        tagIds.forEach(tagId -> {
            questionTagList.add(new QuestionTag(
                    questionId,
                    tagId
            ));
        });
        questionTagRepository.saveAll(questionTagList);

        return new QuestionCreateResponseVo(questionId);
    }

    @Override
    public QuestionQueryResponseVo queryQuestion(QuestionQueryRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }

        Question question = questionRepository.findById(request.getId()).orElse(null);
        if (question == null) {
            throw new HuhuException(ErrMessage.NOT_FOUND_QUESTION);
        }


        return new QuestionQueryResponseVo(
                question.getTitle(),
                question.getContent(),
                question.getCommentCount(),
                question.getFollowerCount(),
                question.getAnswerCount(),
                question.getStatus(),
                question.getCreateTime(),
                question.getUpdateTime()
        );
    }

    @Override
    @Transactional
    public QuestionUpdateResponseVo updateQuestion(QuestionUpdateRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }

        Question question = questionRepository.findByIdAndUserId(request.getId(), request.getUserId());
        if (question == null
                || question.getStatus() >= QuestionStatus.CLOSE.getCode()) {
            throw new HuhuException(ErrMessage.NOT_ALLOW_ACTION);
        }

        if (questionRepository.existsByTitle(request.getTitle())) {
            throw new HuhuException(ErrMessage.EXIST_SAME_TITLE);
        }

        question = request.update(question);
        questionRepository.save(question);

        return new QuestionUpdateResponseVo(request.getId());
    }

    @Override
    @Transactional
    public QuestionDeleteResponseVo deleteQuestion(QuestionDeleteRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }

        //防止他人修改问题
        Question question = questionRepository.findByIdAndUserId(request.getId(), request.getUserId());
        if (question == null
                || question.getStatus() == QuestionStatus.CLOSE.getCode()) {
            throw new HuhuException(ErrMessage.NOT_ALLOW_ACTION);
        }

        question.setStatus(QuestionStatus.CLOSE.getCode());
        questionRepository.save(question);
        return new QuestionDeleteResponseVo(question.getId());
    }

    @Override
    public QuestionQueryAllResponseVo queryAll(QuestionQueryAllRequestVo request) {

        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Page<Question> questions = questionRepository.findAll(PageRequest.of(
                request.getPageNum(),
                request.getPageSize(),
                sort
        ));

        List<QuestionQueryAllResponseVo.QuestionInfo> questionInfoList = Lists.newArrayList();
        questions.forEach(question -> {
            questionInfoList.add(new QuestionQueryAllResponseVo.QuestionInfo(
                    question.getId(),
                    question.getTitle(),
                    question.getCreateTime(),
                    question.getUpdateTime()
            ));
        });

        return new QuestionQueryAllResponseVo(
                questionInfoList,
                request.getPageNum(),
                request.getPageSize()
        );
    }


    @Override
    @Transactional
    public void followQuestion(QuestionFollowRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }

        if (!questionRepository.existsById(request.getQuestionId())) {
            throw new HuhuException(ErrMessage.NOT_FOUND_QUESTION);
        }

        QuestionFollower questionFollower = questionFollowerRepository.findByQuestionIdAndFollowerId(
                request.getQuestionId(),
                request.getUserId());
        if (questionFollower != null) {
            throw new HuhuException(ErrMessage.EXIST_QUESTION_FOLLOWER);
        }

        questionFollower = new QuestionFollower(
                request.getQuestionId(),
                request.getUserId()
        );

        questionFollowerRepository.save(questionFollower);

        //增加问题的关注数
        questionRepository.incrementFollowerCountById(request.getQuestionId());

    }

    @Override
    @Transactional
    public void unFollowQuestion(QuestionUnFollowRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }

        if (!questionRepository.existsById(request.getQuestionId())) {
            throw new HuhuException(ErrMessage.NOT_FOUND_QUESTION);
        }

        QuestionFollower questionFollower = questionFollowerRepository.findByQuestionIdAndFollowerId(
                request.getQuestionId(),
                request.getUserId());
        if (questionFollower == null) {
            throw new HuhuException(ErrMessage.NOT_EXIST_QUESTION_FOLLOWER);
        }

        //删除记录
        questionFollowerRepository.deleteById(questionFollower.getId());

        //减少question的followerCount
        questionRepository.decrementFollowerCountById(request.getQuestionId());
    }

    /**
     * 检查是指如果数据库中还不存在用户给出的tag，那么就创建，否则就直接获取tagId即可
     * @param tagNames 用户的tag集合
     * @return tag id集合
     */
    private Set<Long> checkAndFilterTags(Set<String> tagNames) {
        Set<Long> tagIds = Sets.newHashSet();
        for (String tagName : tagNames) {
            if (!StringUtils.isNotEmpty(tagName)) {
                continue;
            }
            Tag tag = tagRepository.findByName(tagName);
            if (tag == null) {
                tag = tagRepository.save(new Tag(tagName));
            }
            tagIds.add(tag.getId());
        }
        return tagIds;
    }
}
