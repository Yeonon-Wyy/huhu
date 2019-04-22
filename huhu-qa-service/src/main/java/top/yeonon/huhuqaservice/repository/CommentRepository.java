package top.yeonon.huhuqaservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.yeonon.huhuqaservice.entity.Comment;

import java.util.Collection;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 18:36
 **/
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByIdIn(Collection<Long> ids, Pageable pageable);
}
