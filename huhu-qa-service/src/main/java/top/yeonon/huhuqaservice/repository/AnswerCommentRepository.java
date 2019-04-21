package top.yeonon.huhuqaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.yeonon.huhuqaservice.entity.AnswerComment;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 18:51
 **/
@Repository
public interface AnswerCommentRepository extends JpaRepository<AnswerComment, Long> {

    /**
     * 判断是否存在符合条件的answer记录
     * @param userId 用户ID
     * @param answerId 回答ID
     * @return true表示存在，false表示不存在
     */
    boolean existsByUserIdAndAnswerId(Long userId, Long answerId);
}
