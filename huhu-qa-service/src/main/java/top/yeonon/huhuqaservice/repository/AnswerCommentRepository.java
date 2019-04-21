package top.yeonon.huhuqaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.yeonon.huhuqaservice.entity.Answer;
import top.yeonon.huhuqaservice.entity.AnswerComment;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 18:51
 **/
@Repository
public interface AnswerCommentRepository extends JpaRepository<AnswerComment, Long> {

    boolean existsByUserIdAndAnswerId(Long userId, Long answerId);
}
