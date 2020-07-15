package com.sunlee.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author sunlee
 * @date 2020/2/28 23:09
 */
@Entity
@Data
public class IndexBanner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String imgUrl;
    private Integer bannerOrder;
}
