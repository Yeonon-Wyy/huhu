package top.yeonon.huhuqaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.yeonon.huhuqaservice.entity.QuestionAnswer;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 18:48
 **/
@Repository
public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Long> {
}
