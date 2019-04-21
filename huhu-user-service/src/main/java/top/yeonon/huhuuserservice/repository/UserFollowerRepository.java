package top.yeonon.huhuuserservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.yeonon.huhuuserservice.entity.UserFollower;

import java.util.List;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 17:06
 **/
@Repository
public interface UserFollowerRepository extends JpaRepository<UserFollower, Long> {

    /**
     * 判断是否存在满足条件的记录
     * @param userId 用户ID
     * @param followerUserId 关注者ID
     * @return true表示存在，false表示不存在
     */
    boolean existsByUserIdAndFollowerId(Long userId, Long followerUserId);

    /**
     * 根据用户ID和关注者ID删除记录（即删除关注记录）
     * @param userId 用户ID
     * @param followerUserId 关注者ID
     */
    void deleteByUserIdAndFollowerId(Long userId, Long followerUserId);

    /**
     * 根据用户ID查找其关注者列表
     * @param userId 用户ID
     * @return 关注者列表
     */
    List<UserFollower> findByUserId(Long userId);
}
