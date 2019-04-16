package top.yeonon.huhuuserservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.yeonon.huhuuserservice.entity.User;

import java.util.Collection;
import java.util.List;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 15:08
 **/
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    boolean existsByIdAndUsername(Long id, String username);

    Page<User> findAllByIdIn(Collection<Long> ids, Pageable pageRequest);



}
