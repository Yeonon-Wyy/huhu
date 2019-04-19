package top.yeonon.huhuqaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.yeonon.huhuqaservice.entity.QuestionTag;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 18:50
 **/
@Repository
public interface QuestionTagRepository extends JpaRepository<QuestionTag, Long> {
}
