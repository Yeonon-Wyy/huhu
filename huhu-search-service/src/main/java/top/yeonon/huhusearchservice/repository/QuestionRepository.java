package top.yeonon.huhusearchservice.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import top.yeonon.huhusearchservice.entity.Question;

/**
 * @Author yeonon
 * @date 2019/4/29 0029 20:52
 **/
@Repository
public interface QuestionRepository extends ElasticsearchRepository<Question, String> {
}
