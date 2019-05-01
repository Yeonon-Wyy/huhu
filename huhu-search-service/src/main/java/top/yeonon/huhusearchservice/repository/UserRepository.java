package top.yeonon.huhusearchservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import top.yeonon.huhusearchservice.entity.User;

/**
 * @Author yeonon
 * @date 2019/4/30 0030 19:19
 **/
@Repository
public interface UserRepository extends ElasticsearchRepository<User, String> {

    Page<User> findAllByUsernameLikeOrProfileLike(String username, String profile, Pageable pageable);
}
