package top.yeonon.huhuqaservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.yeonon.huhuqaservice.constant.AnswerStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 18:24
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "answer")
public class Answer {

    @Id
    @Column(name = "id", nullable = false, length = 11)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 11)
    private Long userId;

    @Column(name = "question_id", nullable = false, length = 11)
    private Long questionId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "status", nullable = false, length = 4)
    private Integer status;

    @Column(name = "approval_count", nullable = false, length = 50)
    private Long approvalCount;

    @Column(name = "comment_count", nullable = false, length = 50)
    private Long commentCount;

    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Column(name = "update_time", nullable = false)
    private Date updateTime;

    public Answer(Long userId, Long questionId, String content) {
        this.userId = userId;
        this.questionId = questionId;
        this.content = content;

        this.status = AnswerStatus.NORMAL.getCode();
        this.approvalCount = 0L;
        this.commentCount = 0L;
        this.createTime = new Date();
        this.updateTime = this.createTime;
    }

    public static void main(String[] args) {
        List<Integer> nums = new ArrayList<>();
        nums.add(null);
        System.out.println(nums.size());
    }
}
