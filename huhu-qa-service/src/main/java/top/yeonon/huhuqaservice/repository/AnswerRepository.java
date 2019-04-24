package top.yeonon.huhuqaservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.yeonon.huhuqaservice.entity.Answer;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 18:30
 **/
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    /**
     * 根据用户ID和问题ID查找记录
     * @param userId 用户ID
     * @param questionId 问题ID
     * @return 存在则返回记录，否则返回null
     */
    Answer findByUserIdAndQuestionId(Long userId, Long questionId);

    /**
     * 根据问题ID分页的查找记录
     * @param questionId 问题ID
     * @param pageable 分页对象
     * @return 包含分页信息以及记录信息的对象
     */
    Page<Answer> findAllByQuestionId(Long questionId, Pageable pageable);

    /**
     * 通过问题ID和用户ID查找记录
     * @param id 问题ID
     * @param userId 用户ID
     * @return 存在则返回实际记录，否则返回null
     */
    Answer findByIdAndUserId(Long id, Long userId);

    /**
     * 根据用户ID查找记录（分页的方式）
     * @param userId 用户ID
     * @param pageable 分页对象
     * @return 包含分页信息以及记录信息的对象
     */
    Page<Answer> findAllByUserId(Long userId, Pageable pageable);

    /**
     * 增加评论数量
     * @param id 回答ID
     */
    @Modifying
    @Query("update Answer a set a.commentCount = a.commentCount + 1 where a.id = :id")
    void incrementCommentCountById(@Param("id") Long id);

    @Modifying
    @Query("update Answer a set a.approvalCount = :approvalCount where a.id = :id")
    void updateApprovalCount(@Param("approvalCount") Long approvalCount,
                             @Param("id") Long id);
}
