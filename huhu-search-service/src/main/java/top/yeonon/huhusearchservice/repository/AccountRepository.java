package top.yeonon.huhusearchservice.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import top.yeonon.huhusearchservice.entity.Account;

/**
 * @Author yeonon
 * @date 2019/4/29 0029 19:06
 **/
@Repository
public interface AccountRepository extends ElasticsearchRepository<Account, String> {
}
