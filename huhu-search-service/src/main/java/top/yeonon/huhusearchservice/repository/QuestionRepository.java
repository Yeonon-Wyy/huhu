package top.yeonon.huhusearchservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import top.yeonon.huhusearchservice.entity.Question;

import java.util.List;

/**
 * @Author yeonon
 * @date 2019/4/29 0029 20:52
 **/
@Repository
public interface QuestionRepository extends ElasticsearchRepository<Question, String> {


    Page<Question> findAllByTitleLikeOrContentLike(String title, String content, Pageable pageable);
}
