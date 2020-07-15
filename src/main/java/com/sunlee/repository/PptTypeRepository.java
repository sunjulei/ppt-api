package com.sunlee.repository;

import com.sunlee.entity.PptType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author sunlee
 * @date 2020/2/14 16:37
 */
public interface PptTypeRepository extends JpaRepository<PptType,Integer> {

    @Query(value ="select c from PptType c where c.menuName=?1 order by c.order")
    List<PptType> findAllByMenuName(String menuName);
}
