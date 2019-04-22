package top.yeonon.huhuqaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.yeonon.huhuqaservice.entity.AnswerComment;

import java.util.List;

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

    /**
     * 根据回答ID查找所有的commentId
     * @param answerId 回答ID
     * @return commentId 列表
     */
    @Query("select ac.commentId from AnswerComment ac where ac.answerId = :answerId")
    List<Long> findCommentIdByAnswerId(@Param("answerId") Long answerId);
}
