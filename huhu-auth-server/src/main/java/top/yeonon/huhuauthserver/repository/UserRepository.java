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

    /**
     * 根据用户名查找用户对象
     * @param username 用户名
     * @return 存在则返回相应记录，否则返回null
     */
    User findByUsername(String username);

}
