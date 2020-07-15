package com.sunlee.repository;

import com.sunlee.entity.Backup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author sunlee
 * @date 2020/4/24 23:13
 */
public interface BackRepository extends JpaRepository<Backup,Integer> {

    @Query(value = "SELECT c FROM Backup c ORDER BY c.id DESC")
    Page<Backup> findAllByTime(PageRequest pr);
}
