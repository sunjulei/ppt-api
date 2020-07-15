package com.sunlee.repository;

import com.sunlee.entity.PptMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author sunlee
 * @date 2020/2/14 16:37
 */
public interface PptMenuRepository extends JpaRepository<PptMenu, Integer> {

    @Query(value = "select c.name from PptMenu c where c.id =?1")
    String findMenuById(Integer menuId);
}
