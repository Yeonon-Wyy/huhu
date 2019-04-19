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
}
