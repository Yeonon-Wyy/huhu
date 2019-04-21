package top.yeonon.huhuuserservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.yeonon.huhuuserservice.entity.UserFollowing;

import java.util.List;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 17:06
 **/
@Repository
public interface UserFollowingRepository extends JpaRepository<UserFollowing, Long> {

    /**
     * 判断是否存在满足条件的记录
     * @param userId 用户ID
     * @param followingId 关注的用户ID
     * @return true表示存在，false表示不存在
     */
    boolean existsByUserIdAndFollowingId(Long userId, Long followingId);

    /**
     * 根据用户ID和关注该用户的用户ID删除记录
     * @param userId 用户ID
     * @param followingId 关注该用户的用户ID
     */
    void deleteByUserIdAndFollowingId(Long userId, Long followingId);

    /**
     * 根据用户ID查找“我关注的用户”列表
     * @param userId 用户ID
     * @return “我关注的用户”列表
     */
    List<UserFollowing> findByUserId(Long userId);
}
