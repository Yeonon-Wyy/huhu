package top.yeonon.huhuqaservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 18:50
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "answer_comment")
public class AnswerComment {

    @Id
    @Column(name = "id", nullable = false, length = 11)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "answer_id", nullable = false, length = 11)
    private Long answerId;

    @Column(name = "comment_id", nullable = false, length = 11)
    private Long commentId;

    @Column(name = "user_id", nullable = false, length = 11)
    private Long userId;

    public AnswerComment(Long answerId, Long commentId, Long userId) {
        this.answerId = answerId;
        this.commentId = commentId;
        this.userId = userId;
    }
}
