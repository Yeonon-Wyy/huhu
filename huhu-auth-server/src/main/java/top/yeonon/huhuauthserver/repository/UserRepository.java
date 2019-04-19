package top.yeonon.huhuauthserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.yeonon.huhuauthserver.entity.User;

/**
 * @Author yeonon
 * @date 2019/4/16 0016 16:01
 **/
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

}
