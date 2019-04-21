package top.yeonon.huhuqaservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.yeonon.huhuqaservice.entity.Answer;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 18:30
 **/
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Answer findByUserIdAndQuestionId(Long userId, Long questionId);

    Page<Answer> findAllByQuestionId(Long questionId, Pageable pageable);

    Answer findByIdAndUserId(Long id, Long userId);

    Page<Answer> findAllByUserId(Long userId, Pageable pageable);
}
