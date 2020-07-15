package com.sunlee.repository;

import com.sunlee.entity.FileHistoryCollect;
import com.sunlee.entity.FileHistoryDown;
import com.sunlee.entity.PptFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author sunlee
 * @date 2020/3/23 22:14
 */
public interface FileHistoryCollectRepository extends JpaRepository<FileHistoryCollect,Integer> {

    @Query(value = "select c  from FileHistoryCollect c where c.username=?1")
    Page<FileHistoryCollect> findAllByUsername(String username, PageRequest request);

    @Query(value = "select c.id  from FileHistoryCollect c where c.username=?1 and c.fileId=?2")
    Integer findIsPresence(String username, Integer fileId);

    @Transactional
    @Modifying
    @Query(value = "delete from FileHistoryCollect c where c.username=?1 and c.fileId=?2")
    void deleteByFileIdAndUsername(String username, Integer fileId);

    @Query(value = "select c  from FileHistoryCollect c where c.fileId=?1")
    List<FileHistoryCollect> findCollNumByFileId(Integer fileId);
}
