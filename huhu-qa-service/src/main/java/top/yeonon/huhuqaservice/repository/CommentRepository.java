package top.yeonon.huhuqaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.yeonon.huhuqaservice.entity.Comment;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 18:36
 **/
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


}
