package com.sunlee.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author sunlee
 * @date 2020/3/22 14:21
 */
@Entity
@Data
public class FileHistoryDown {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer fileId;
    private String username;
    private String downTime;

}
