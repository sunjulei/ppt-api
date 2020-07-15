package com.sunlee.repository;

import com.sunlee.entity.FileHistoryRecord;
import com.sunlee.entity.IndexBanner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author sunlee
 * @date 2020/3/22 14:22
 */
public interface FileHistoryRecordRepository extends JpaRepository<FileHistoryRecord, Integer> {

    @Query(value = "select c  from FileHistoryRecord c where c.username=?1")
    Page<FileHistoryRecord> findAllByUsername(String username, PageRequest request);

    @Query(value = "select c.id  from FileHistoryRecord c where c.username=?1 and c.fileId=?2")
    Integer findIsPresence(String username, Integer fileId);
}
