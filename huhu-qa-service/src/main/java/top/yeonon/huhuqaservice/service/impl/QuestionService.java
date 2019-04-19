package top.yeonon.huhuqaservice.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhuqaservice.constant.ErrMessage;
import top.yeonon.huhuqaservice.entity.Question;
import top.yeonon.huhuqaservice.entity.QuestionTag;
import top.yeonon.huhuqaservice.entity.Tag;
import top.yeonon.huhuqaservice.repository.QuestionRepository;
import top.yeonon.huhuqaservice.repository.QuestionTagRepository;
import top.yeonon.huhuqaservice.repository.TagRepository;
import top.yeonon.huhuqaservice.service.IQuestionService;
import top.yeonon.huhuqaservice.vo.request.QuestionCreateRequestVo;
import top.yeonon.huhuqaservice.vo.response.QuestionCreateResponseVo;

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

    @Autowired
    public QuestionService(QuestionRepository questionRepository,
                           QuestionTagRepository questionTagRepository, TagRepository tagRepository) {
        this.questionRepository = questionRepository;
        this.questionTagRepository = questionTagRepository;
        this.tagRepository = tagRepository;
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
