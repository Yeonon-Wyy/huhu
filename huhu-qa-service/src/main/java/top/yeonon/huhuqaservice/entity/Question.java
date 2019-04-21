package top.yeonon.huhuqaservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import top.yeonon.huhuqaservice.constant.QuestionStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 17:49
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "question")
public class Question {

    @Id
    @Column(name = "id", nullable = false, length = 11)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "follower_count", nullable = false, length = 20)
    private Integer followerCount;

    @Column(name = "answer_count", nullable = false, length = 20)
    private Integer answerCount;

    @Column(name = "comment_count", nullable = false, length = 20)
    private Integer commentCount;

    @Column(name = "status", nullable = false, length = 4)
    private Integer status;

    @Column(name = "user_id", nullable = false, length = 11)
    private Long userId;

    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Column(name = "update_time", nullable = false)
    private Date updateTime;

    public Question(String title, String content, Long userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;

        this.commentCount = 0;
        this.followerCount = 0;
        this.answerCount = 0;
        this.status = QuestionStatus.NORMAL.getCode();
        this.createTime = new Date();
        this.updateTime = this.createTime;
    }
}
