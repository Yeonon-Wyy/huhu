package top.yeonon.huhuuserservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.yeonon.huhuuserservice.entity.User;

import java.util.Collection;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 15:08
 **/
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 判断是否存在满足条件的记录
     * @param username 用户名
     * @return true表示存在，false表示不存在
     */
    boolean existsByUsername(String username);

    /**
     * 判断是否存在满足条件的记录
     * @param username 用户名
     * @param email 邮箱
     * @return true表示存在，false表示不存在
     */
    boolean existsByUsernameAndEmail(String username, String email);

    /**
     * 根据ID列表分页的查询记录
     * @param ids id列表
     * @param pageRequest 页请求对象（包含size,num,sort等）
     * @return 包含用户信息的分页对象
     */
    Page<User> findAllByIdIn(Collection<Long> ids, Pageable pageRequest);

    /**
     * 根据用户名查找记录
     * @param username 用户名
     * @return 存在则返回相应记录，否则返回null
     */
    User findByUsername(String username);


    @Modifying
    @Query("update User u set u.avatar = :avatarFilename where u.id = :id")
    void updateAvatarById(@Param("avatarFilename") String avatarFilename, @Param("id") Long id);

}
