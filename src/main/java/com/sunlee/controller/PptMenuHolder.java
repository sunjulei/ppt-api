package com.sunlee.controller;

import com.sunlee.entity.PptMenu;
import com.sunlee.repository.PptMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author sunlee
 * @date 2020/2/20 13:22
 */
@RestController
@RequestMapping("/pptMenu")
public class PptMenuHolder {

    @Autowired
    PptMenuRepository pptMenuRepository;

    @GetMapping("/findAll")
    List<PptMenu> findAll(){
        return pptMenuRepository.findAll();
    }

    String findMenuById(Integer id){
        return pptMenuRepository.findMenuById(id);
    }

}
