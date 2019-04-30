package top.yeonon.huhusearchservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import top.yeonon.huhusearchservice.entity.Answer;

/**
 * @Author yeonon
 * @date 2019/4/30 0030 16:57
 **/
@Repository
public interface AnswerRepository extends ElasticsearchRepository<Answer, String> {

    Page<Answer> findAllByContentLike(String content, Pageable pageable);
}

