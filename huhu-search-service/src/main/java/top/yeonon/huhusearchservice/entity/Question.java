package top.yeonon.huhusearchservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import top.yeonon.huhusearchservice.constant.ElasticSearchConst;
import top.yeonon.huhusearchservice.mysql.listener.vo.Suggest;

import java.util.Date;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 17:49
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(indexName = ElasticSearchConst.Question.INDEX_NAME, type = ElasticSearchConst.Question.TYPE_NAME)
public class Question {

    @Id
    private String id;

    private String title;

    private String content;

    private Integer followerCount;

    private Integer answerCount;

    private Integer commentCount;

    private Integer status;

    private Long userId;

    private Date createTime;

    private Date updateTime;

    private Suggest suggest;

}
