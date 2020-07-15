package com.sunlee.controller.others;

import com.sunlee.entity.Others.HelpSuggest;
import com.sunlee.entity.PptFile;
import com.sunlee.repository.others.HelpSuggestRepository;
import com.sunlee.utils.result.Result;
import com.sunlee.utils.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author sunlee
 * @date 2020/4/13 9:33
 */
@RestController
@RequestMapping("/helpSuggest")
public class HelpSuggestController {

    @Autowired
    HelpSuggestRepository help;

    @PostMapping("/add")
    private Result add(@RequestBody HelpSuggest helpSuggest){

        Date date = new Date();
        helpSuggest.setStartTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date));
        try {
            help.save(helpSuggest);
            return ResultFactory.buildSuccessResult(helpSuggest);
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e+"");
        }
    }

    @GetMapping("/findAll")
    private Result findAll(){
        try {
            List<HelpSuggest> lists = help.findAll();
            return ResultFactory.buildSuccessResult(lists);
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e+"");
        }
    }

    @DeleteMapping("/deleteById/{id}")
    private Result deleteAll(@PathVariable Integer id) {
        try {
            help.deleteById(id);
            return ResultFactory.buildSuccessResult(id);
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e+"");
        }
    }

    @PostMapping("/deleteAll")
    private Result deleteAll(@RequestBody List<HelpSuggest> helpSuggests) {
        try {
            help.deleteAll(helpSuggests);
            return ResultFactory.buildSuccessResult(helpSuggests);
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e+"");
        }
    }

    @GetMapping("/todayNum")
    public Result findTodayNum() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        List<HelpSuggest> list = help.findTodayNum(date);

        return ResultFactory.buildSuccessResult(list.size());
    }
}
