package top.yeonon.huhuqaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.yeonon.huhuqaservice.entity.QuestionComment;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 18:49
 **/
@Repository
public interface QuestionCommentRepository extends JpaRepository<QuestionComment, Long> {

    /**
     * 判断是否存在满足条件的记录
     * @param userId 用户ID
     * @param questionId 问题ID
     * @return true表示存在，false表示不存在
     */
    boolean existsByUserIdAndQuestionId(Long userId, Long questionId);
}
