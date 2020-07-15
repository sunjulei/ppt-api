package com.sunlee.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author sunlee
 * @date 2020/4/6 12:06
 */
@Entity
@Data
public class SysBackground {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String imgType;
    private String imgUrl;
}
