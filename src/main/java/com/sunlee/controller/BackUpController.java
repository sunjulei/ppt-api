package com.sunlee.controller;

import com.sunlee.entity.Backup;
import com.sunlee.repository.BackRepository;
import com.sunlee.utils.CopyFile;
import com.sunlee.utils.result.Result;
import com.sunlee.utils.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 备份文件和数据库
 *
 * @author sunlee
 * @date 2020/4/24 15:27
 */
@RestController
public class BackUpController {

    @Autowired
    BackRepository backRepository;

    @PostMapping("/backup")
    private Result backup(@RequestBody Backup backUp) throws Exception {

        String realPath = ClassUtils.getDefaultClassLoader().getResource("").getPath().substring(1);
        String path = realPath + "static";

        //日期
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String backTime = format.format(new Date());
        //路径
        File targetFile = new File(backUp.getPath());
        if (!targetFile.exists()) {
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
        }

        try {

            CopyFile.copyFolder(path, backUp.getPath());
            databasebackup(backUp.getPath());

            backUp.setPath(targetFile.getAbsolutePath());
            backUp.setEndTime(backTime);
            backRepository.save(backUp);
            return ResultFactory.buildSuccessResult("备份成功");
        } catch (Exception e) {
            return ResultFactory.buildFailResult("备份异常，请检查路径");
        }

    }


    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;
    @Value("${mydb.dbname}")
    String dbName;

    public void databasebackup(String filePath) {
        filePath = filePath + "/" + dbName + new Date().getTime() + ".sql";
        String realPath = ClassUtils.getDefaultClassLoader().getResource("").getPath().substring(1);
        String path = realPath + "static/mysqldump";
        try {
            String cmd = "cmd /c " + path + " -h " + "127.0.0.1" + " -P "
                    + "3306" + " -u " + username + " -p" + password + " " + dbName + ">" + filePath;
            Runtime.getRuntime().exec(cmd);
            System.out.println(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/backupList")
    public Result backupList() {

        PageRequest pr =PageRequest.of(0, 10);

        Page<Backup> all = backRepository.findAllByTime(pr);

        return ResultFactory.buildSuccessResult(all);
    }
}
