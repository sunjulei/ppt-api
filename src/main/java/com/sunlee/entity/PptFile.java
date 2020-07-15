package com.sunlee.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author sunlee
 * @date 2020/2/15 21:23
 */
@Entity
@Data
public class PptFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String downUrl;
    private String tag;
    private String title;
    private String type;
    private String menu;
    private String size;
    private String fileDesc;
    private Integer collectNum;
    private Integer downNum;
    private Integer pagNum;
    private String author;
    private String roughImgUrl;
    private String detailImgUrl;
    private String upData;
    private String videoUrl;
    private String state;

}
