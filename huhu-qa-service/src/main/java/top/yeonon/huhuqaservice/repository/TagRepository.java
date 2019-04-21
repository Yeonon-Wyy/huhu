package top.yeonon.huhuqaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.yeonon.huhuqaservice.entity.Tag;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 19:20
 **/
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * 通过名称查找记录
     * @param name 名称
     * @return 存在则返回相应记录，否则返回null
     */
    Tag findByName(String name);
}
