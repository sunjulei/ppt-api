package com.sunlee.repository;

import com.sunlee.entity.PptType;
import com.sunlee.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author sunlee
 * @date 2020/3/1 11:42
 */
public interface UserRepository extends JpaRepository<User,Integer> {

    @Query(value = "select u from User u where u.username=?1")
    public User queryUserByName(String username);

    @Query(value = "select u from User u where u.registerTime=?1")
    List<User> findTodayNum(String date);

    @Query(value = "select u from User u order by u.id desc ")
    Page<User> findAll(Pageable pg);


}
