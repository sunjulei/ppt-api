package com.sunlee.repository;

import com.sunlee.entity.SessionList;
import com.sunlee.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author sunlee
 * @date 2020/3/10 12:22
 */

public interface SessionListRepository extends JpaRepository<SessionList, Integer> {



    @Transactional
    @Modifying
    @Query(value = "update session_list c set c.state = ?1 , c.end_time = ?2  where c.session_id=?3",nativeQuery=true)
    void updateBySessId(String state, String endTime, String sessionId);

    @Query(value = "select u from SessionList u order by u.startTime desc ")
    Page<SessionList> findAll(Pageable pg);
}
