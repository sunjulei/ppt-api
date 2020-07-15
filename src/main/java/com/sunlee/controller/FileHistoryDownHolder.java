package com.sunlee.controller;

import com.sunlee.entity.FileHistoryCollect;
import com.sunlee.entity.FileHistoryDown;
import com.sunlee.entity.PptFile;
import com.sunlee.repository.FileHistoryDownRepository;
import com.sunlee.repository.PptFileRepository;
import com.sunlee.utils.result.Result;
import com.sunlee.utils.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author sunlee
 * @date 2020/3/23 22:15
 */
@RestController
@RequestMapping("/fileDown")
public class FileHistoryDownHolder {

    @Autowired
    FileHistoryDownRepository fileDown;

    @Autowired
    PptFileRepository pptFile;

    @PostMapping("/add")
    public Result save(@RequestBody FileHistoryDown file) {
        try {
            Integer currentId = fileIsPresence(file);

            if (currentId != 0) {
                //多次访问该文件，只需更新时间
                file.setId(currentId);
            }

            Date date = new Date();
            String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            file.setDownTime(endTime);

            fileDown.save(file);
            return ResultFactory.buildSuccessResult("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultFactory.buildFailResult("保存失败");
        }
    }

    @GetMapping("/findAll/{username}/{page}/{size}")
    public Result findAll(@PathVariable String username,
                          @PathVariable Integer page,
                          @PathVariable Integer size) {

        try {
            PageRequest request = PageRequest.of(page, size);
            Page<FileHistoryDown> allFileHist = fileDown.findAllByUsername(username, request);

            List<Integer> list = new ArrayList<>();
            for (FileHistoryDown f : allFileHist) {

                list.add(f.getFileId());
            }

            List<PptFile> allById = pptFile.findAllById(list);

            Map<String, Object> map = new HashMap<>();
            map.put("files", allFileHist);
            map.put("filesData", allById);

            return ResultFactory.buildSuccessResult(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultFactory.buildFailResult("查询发生错误");
        }
    }

    Integer fileIsPresence(FileHistoryDown file) {

        Integer object = fileDown.findIsPresence(file.getUsername(), file.getFileId());

        if (object != null) {
            return object;
        } else {
            return 0;
        }
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {

        try {
            fileDown.deleteById(id);
            return ResultFactory.buildSuccessResult(id);
        } catch (Exception e) {
            return ResultFactory.buildFailResult("历史记录删除失败"+e);
        }

    }

    @PostMapping("/deleteAll")
    public Result deleteAll(@RequestBody List<FileHistoryDown> fileList) {
        try {
            fileDown.deleteAll(fileList);
            return ResultFactory.buildSuccessResult(fileList);
        } catch (Exception e) {
            return ResultFactory.buildFailResult("历史记录删除失败"+e);
        }

    }

    @GetMapping("/countNum/{fileId}")
    public Result findDownNumByFileId(@PathVariable Integer fileId) {
        List<FileHistoryDown> coll = fileDown.findDownNumByFileId(fileId);
        return ResultFactory.buildSuccessResult(coll.size());
    }
}
