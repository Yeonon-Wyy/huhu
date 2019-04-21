package top.yeonon.huhuqaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.yeonon.huhuqaservice.entity.QuestionFollower;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 18:49
 **/
@Repository
public interface QuestionFollowerRepository extends JpaRepository<QuestionFollower, Long> {

    /**
     * 根据问题ID和关注者（用户）ID查找记录
     * @param questionId 问题ID
     * @param followerId 关注者ID
     * @return 存在则返回相应记录，否则返回null
     */
    QuestionFollower findByQuestionIdAndFollowerId(Long questionId, Long followerId);
}
