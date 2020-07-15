package com.sunlee.repository.others;

import com.sunlee.entity.Others.HelpSuggest;
import com.sunlee.entity.PptFile;
import com.sunlee.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author sunlee
 * @date 2020/4/13 9:32
 */
public interface HelpSuggestRepository extends JpaRepository<HelpSuggest, Integer> {

    @Query(value = "select c  from HelpSuggest c where c.startTime like %?1%")
    List<HelpSuggest> findTodayNum(String date);

    @Query(value = "select u from HelpSuggest u order by u.startTime desc ")
    List<HelpSuggest> findAll();
}
