package top.yeonon.huhuuserservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.yeonon.huhuuserservice.entity.User;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 15:08
 **/
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);


}
