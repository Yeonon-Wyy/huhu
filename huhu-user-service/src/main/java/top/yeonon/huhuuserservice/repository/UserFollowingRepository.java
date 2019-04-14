package top.yeonon.huhuuserservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.yeonon.huhuuserservice.entity.UserFollowing;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 17:06
 **/
@Repository
public interface UserFollowingRepository extends JpaRepository<UserFollowing, Long> {

    boolean existsByUserIdAndFollowingId(Long userId, Long followingId);

    void deleteByUserIdAndFollowingId(Long userId, Long followingId);
}
