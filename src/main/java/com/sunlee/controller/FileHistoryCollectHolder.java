package com.sunlee.controller;

import com.sunlee.entity.FileHistoryCollect;
import com.sunlee.entity.PptFile;
import com.sunlee.repository.FileHistoryCollectRepository;
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
 * @date 2020/3/29 13:50
 */
@RestController
@RequestMapping("/fileCollect")
public class FileHistoryCollectHolder {

    @Autowired
    FileHistoryCollectRepository fileCollect;

    @Autowired
    PptFileRepository pptFile;

    @PostMapping("/add")
    public Result save(@RequestBody FileHistoryCollect file) {
        try {
            Integer currentId = fileIsPresence(file);

            if (currentId != 0) {
                //多次访问该文件，只需更新时间
                file.setId(currentId);
            }

            Date date = new Date();
            String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            file.setCollectTime(endTime);

            fileCollect.save(file);
            return ResultFactory.buildSuccessResult("收藏成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultFactory.buildFailResult("收藏失败");
        }
    }

    @GetMapping("/findAll/{username}/{page}/{size}")
    public Result findAll(@PathVariable String username,
                          @PathVariable Integer page,
                          @PathVariable Integer size) {

        try {
            PageRequest request = PageRequest.of(page, size);
            Page<FileHistoryCollect> allFileHist = fileCollect.findAllByUsername(username, request);

            List<Integer> list = new ArrayList<>();
            for (FileHistoryCollect f : allFileHist) {

                list.add(f.getFileId());
            }

            List<PptFile> allById = pptFile.findAllById(list);

            Map<String, Object> map = new HashMap<>();
            map.put("files", allFileHist);
            map.put("filesData", allById);

            return ResultFactory.buildSuccessResult(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultFactory.buildFailResult("收藏文件发生错误");
        }
    }

    Integer fileIsPresence(FileHistoryCollect file) {
        Integer object = fileCollect.findIsPresence(file.getUsername(), file.getFileId());
        if (object != null) {
            return object;
        } else {
            return 0;
        }
    }

    @GetMapping("/fileIsCollect/{username}/{id}")
    Integer fileIsCollect(@PathVariable String username, @PathVariable Integer id) {

        Integer object = null;
        try {
            object = fileCollect.findIsPresence(username, id);
            if (object==null){return 0;}
            return object;
        } catch (Exception e) {
            return 0;
        }
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {

        try {
            fileCollect.deleteById(id);
            return ResultFactory.buildSuccessResult(id);
        } catch (Exception e) {
            return ResultFactory.buildFailResult("收藏记录删除失败" + e);
        }

    }

    @PostMapping("/deleteAll")
    public Result deleteAll(@RequestBody List<FileHistoryCollect> fileList) {
        try {
            fileCollect.deleteAll(fileList);
            return ResultFactory.buildSuccessResult(fileList);
        } catch (Exception e) {
            return ResultFactory.buildFailResult("收藏记录删除失败" + e);
        }

    }

    @PostMapping("/deleteByFileIdAndUsername")
    public Result deleteByFileIdAndUsername(@RequestBody FileHistoryCollect file) {
        try {
            fileCollect.deleteByFileIdAndUsername(file.getUsername(), file.getFileId());
            return ResultFactory.buildSuccessResult("删除成功");
        } catch (Exception e) {
            return ResultFactory.buildFailResult("删除失败" + e);
        }

    }


    @GetMapping("/countNum/{fileId}")
    public Result findCollNumByFileId(@PathVariable Integer fileId) {
        List<FileHistoryCollect> coll = fileCollect.findCollNumByFileId(fileId);
        return ResultFactory.buildSuccessResult(coll.size());
    }
}
