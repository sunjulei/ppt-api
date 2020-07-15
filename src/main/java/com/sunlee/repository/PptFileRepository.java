package com.sunlee.repository;

import com.sunlee.entity.PptFile;
import com.sunlee.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author sunlee
 * @date 2020/2/16 10:07
 */
public interface PptFileRepository extends JpaRepository<PptFile, Integer> {

    @Query(value = "select c from PptFile c where  c.state=?1 and c.menu=?2 and c.tag like %?3% ")
    Page<PptFile> findFileByMenuAndType(String state, String menu, String type, PageRequest page);

    @Query(value = "select c from PptFile c where c.state=?1 and c.menu like %?2% ")
    Page<PptFile> findFileByMenu(String state, String menu, PageRequest page);

    @Query(value = "select c from PptFile c where c.state=?1 and (c.tag like ?2 or c.tag like ?3 or c.tag like ?4 or c.tag like ?5 or c.tag like ?6)")
    Page<PptFile> findFileByTag(String state, String tag1, String tag2, String tag3, String tag4, String tag5, PageRequest page);

    @Query(value = "select c  from PptFile c where c.state=?1 order by (c.downNum*0.6+c.collectNum*0.4)  desc")
    Page<PptFile> findRecom(String state, PageRequest page);

    @Query(value = "select c  from PptFile c where c.state=?1")
    Page<PptFile> findAll(String state, PageRequest page);

    @Query(value = "select c  from PptFile c where c.author=?1")
    Page<PptFile> findAllByAuthor(String author, PageRequest request);

    @Query(value = "select c  from PptFile c where c.upData like %?1%")
    List<PptFile> findTodayNum(String date);


}
