package com.sunlee.repository;

import com.sunlee.entity.IndexBanner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author sunlee
 * @date 2020/2/28 23:10
 */
public interface IndexBannerRepository extends JpaRepository<IndexBanner,Integer> {

    @Query(value = "select c  from IndexBanner c order by c.bannerOrder ")
    Page<IndexBanner> findAll(Pageable pageable);
}
