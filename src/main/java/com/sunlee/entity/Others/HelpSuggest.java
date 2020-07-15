package com.sunlee.entity.Others;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author sunlee
 * @date 2020/4/13 9:27
 */
@Entity
@Data
public class HelpSuggest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public String subject;
    public String detailDesc;
    public String connectInfo;
    public String startTime;
}
