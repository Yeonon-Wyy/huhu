package top.yeonon.huhusearchservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import top.yeonon.huhusearchservice.constant.ElasticSearchConst;

import java.util.Date;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 18:24
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(indexName = ElasticSearchConst.QA.INDEX_NAME, type = ElasticSearchConst.QA.ANSWER_TYPE_NAME)
public class Answer {

    @Id
    private String id;

    private Long userId;

    private Long questionId;

    private String content;

    private Integer status;

    private Long approvalCount;

    private Long commentCount;

    private Date createTime;

    private Date updateTime;

}
