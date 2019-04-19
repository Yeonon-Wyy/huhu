package top.yeonon.huhuqaservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.yeonon.huhuqaservice.constant.CommentStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 18:30
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @Column(name = "id", nullable = false, length = 11)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 11)
    private Long userId;

    @Column(name = "status", nullable = false, length = 4)
    private Integer status;

    //是对问题还是对回答
    @Column(name = "type", nullable = false, length = 4)
    private Integer type;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "approval_count", nullable = false, length = 50)
    private Long approvalCount;

    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Column(name = "update_time", nullable = false)
    private Date updateTime;

    public Comment(Long userId, Integer type, String content) {
        this.userId = userId;
        this.type = type;
        this.content = content;

        this.status = CommentStatus.NORMAL.getCode();
        this.approvalCount = 0L;
        this.createTime = new Date();
        this.updateTime = this.createTime;
    }
}
