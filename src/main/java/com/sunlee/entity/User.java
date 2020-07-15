package com.sunlee.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author sunlee
 * @date 2020/3/1 11:40
 */

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate//插入使用默认值
@DynamicInsert//插入使用默认值
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String name;
    private String headImg;
    private String perms;
    private String role;
    private String registerTime;
}
