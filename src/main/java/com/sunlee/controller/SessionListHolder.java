package com.sunlee.controller;

import com.sunlee.entity.SessionList;
import com.sunlee.repository.SessionListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author sunlee
 * @date 2020/3/10 14:06
 */
@RestController
@RequestMapping("/sessionList")
public class SessionListHolder {


    @Autowired
    SessionListRepository sessionListRepository;

    @PostMapping(value = "/save")
    public void save(@RequestBody SessionList sessionList) {

        Date date = new Date();
        date.setTime(Long.parseLong(sessionList.getStartTime()));
        sessionList.setStartTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date));
        sessionListRepository.save(sessionList);
    }

    @PostMapping(value = "/update")
    public void updateBySessId(@RequestBody SessionList sessionList) {
        Date date = new Date();
        date.setTime(Long.parseLong(sessionList.getEndTime()));
        String endTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);

        sessionListRepository.updateBySessId(sessionList.getState(),endTime,sessionList.getSessionId());
    }

    @GetMapping(value = "/findAll/{page}/{size}")
    public Page<SessionList> findAll(@PathVariable Integer page, @PathVariable Integer size){
        PageRequest request = PageRequest.of(page, size);
        return sessionListRepository.findAll(request);
    }
}
