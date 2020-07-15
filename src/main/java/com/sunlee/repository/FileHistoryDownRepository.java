package com.sunlee.repository;

import com.sunlee.entity.FileHistoryCollect;
import com.sunlee.entity.FileHistoryDown;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author sunlee
 * @date 2020/3/23 22:14
 */
public interface FileHistoryDownRepository extends JpaRepository<FileHistoryDown,Integer> {

    @Query(value = "select c  from FileHistoryDown c where c.username=?1")
    Page<FileHistoryDown> findAllByUsername(String username, PageRequest request);

    @Query(value = "select c.id  from FileHistoryDown c where c.username=?1 and c.fileId=?2")
    Integer findIsPresence(String username, Integer fileId);

    @Query(value = "select c  from FileHistoryDown c where c.fileId=?1")
    List<FileHistoryDown> findDownNumByFileId(Integer fileId);
}
