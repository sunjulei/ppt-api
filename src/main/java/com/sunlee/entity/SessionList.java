package com.sunlee.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author sunlee
 * @date 2020/3/10 12:20
 */
@Entity
@Data
public class SessionList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String sessionId;
    private String user;
    private String state;
    private String startTime;
    private String endTime;
    private String ip;
    private String address;

}
