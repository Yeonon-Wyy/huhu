package top.yeonon.huhuuserservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.yeonon.huhuuserservice.entity.UserFollower;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 17:06
 **/
@Repository
public interface UserFollowerRepository extends JpaRepository<UserFollower, Long> {

    boolean existsByUserIdAndFollowerId(Long userId, Long followerUserId);

    void deleteByUserIdAndFollowerId(Long userId, Long followerUserId);
}
