package top.yeonon.huhuqaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.yeonon.huhuqaservice.entity.Question;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 18:23
 **/
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
