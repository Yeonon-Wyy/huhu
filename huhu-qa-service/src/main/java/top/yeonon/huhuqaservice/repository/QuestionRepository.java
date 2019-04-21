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

    boolean existsByTitle(String title);

    Question findByIdAndUserId(Long id, Long userId);

    @Modifying
    @Query("update Question q set q.answerCount = q.answerCount + 1 where q.id = :id")
    void incrementAnswerCountById(@Param("id") Long id);

    @Modifying
    @Query("update Question q set q.followerCount = q.followerCount + 1 where q.id = :id")
    void incrementFollowerCountById(@Param("id") Long id);

    @Modifying
    @Query("update Question q set q.followerCount = q.followerCount - 1 where q.id = :id")
    void decrementFollowerCountById(@Param("id") Long id);

    @Modifying
    @Query("update Question q set q.commentCount = q.commentCount + 1 where q.id = :id")
    void incrementCommentCountById(@Param("id") Long id);

    @Query("select q.title from Question q WHERE q.id = :id")
    String findTitleById(@Param("id") Long id);

}
