package com.sunlee.controller;

import com.sunlee.entity.PptType;
import com.sunlee.repository.PptTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author sunlee
 * @date 2020/2/14 17:51
 */
@RestController
@RequestMapping("/pptType")
public class PptTypeHolder {
    @Autowired
    PptTypeRepository PptTypeRepository;

    @GetMapping("/findAllByMenuName/{menuName}")
    List<PptType> findAllByMenuName(@PathVariable("menuName") String menuName) {
        return PptTypeRepository.findAllByMenuName(menuName);
    }
}
