package top.yeonon.huhuqaservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 18:47
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "question_tag")
public class QuestionTag {

    @Id
    @Column(name = "id", nullable = false, length = 11)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_id", nullable = false, length = 11)
    private Long questionId;

    @Column(name = "tag_id", nullable = false, length = 11)
    private Long tagId;
}
