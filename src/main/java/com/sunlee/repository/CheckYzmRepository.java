package com.sunlee.repository;

import com.sunlee.entity.CheckYzm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author sunlee
 * @date 2020/4/7 11:51
 */
public interface CheckYzmRepository extends JpaRepository<CheckYzm, Integer> {

    @Query(value = "select c from CheckYzm c where c.username=?1 and c.yzm=?2")
    CheckYzm queryUser(String username, String yzm);

    @Query(value = "select c from CheckYzm c where c.username=?1")
    CheckYzm queryUserByName(String username);
}
