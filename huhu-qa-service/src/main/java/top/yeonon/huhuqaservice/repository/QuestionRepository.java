package top.yeonon.huhuqaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.yeonon.huhuqaservice.entity.Question;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 18:23
 **/
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    /**
     * 判断是否存在满足条件的记录
     * @param title 问题标题
     * @return true表示存在，false表示不存在
     */
    boolean existsByTitle(String title);

    /**
     * 根据问题ID和用户ID查找记录
     * @param id 问题ID
     * @param userId 用户ID
     * @return 存在则返回相应对象，否则返回null
     */
    Question findByIdAndUserId(Long id, Long userId);

    /**
     * 增加AnswerCount字段的数量
     * @param id 问题ID
     */
    @Modifying
    @Query("update Question q set q.answerCount = q.answerCount + 1 where q.id = :id")
    void incrementAnswerCountById(@Param("id") Long id);

    /**
     * 增加followerCount字段的数量
     * @param id 问题ID
     */
    @Modifying
    @Query("update Question q set q.followerCount = q.followerCount + 1 where q.id = :id")
    void incrementFollowerCountById(@Param("id") Long id);

    /**
     * 减少followerCount字段的数量
     * @param id 问题ID
     */
    @Modifying
    @Query("update Question q set q.followerCount = q.followerCount - 1 where q.id = :id")
    void decrementFollowerCountById(@Param("id") Long id);

    /**
     * 增加评论数量
     * @param id 问题ID
     */
    @Modifying
    @Query("update Question q set q.commentCount = q.commentCount + 1 where q.id = :id")
    void incrementCommentCountById(@Param("id") Long id);

    /**
     * 根据ID查找标题字段
     * @param id 问题ID
     * @return 对应的标题，不存在则返回null
     */
    @Query("select q.title from Question q WHERE q.id = :id")
    String findTitleById(@Param("id") Long id);

    /**
     * 获取某用户的提问数量
     * @param userId 用户ID
     * @return 提问数量
     */
    @Query("select COUNT(1) FROM Question q where q.userId=:userId")
    Integer findCountByUserId(@Param("userId") Long userId);

}
