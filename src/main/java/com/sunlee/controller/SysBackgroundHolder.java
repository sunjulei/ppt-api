package com.sunlee.controller;

import com.sunlee.entity.SysBackground;
import com.sunlee.repository.SysBackgroundRepository;
import com.sunlee.utils.result.Result;
import com.sunlee.utils.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author sunlee
 * @date 2020/4/6 12:08
 */
@RestController
@RequestMapping("/background")
public class SysBackgroundHolder {

    @Autowired
    SysBackgroundRepository background;

    @GetMapping("/findAll")
    List<SysBackground> findAll(){
        return background.findAll();
    }

    @PutMapping("/update")
    public Result update(@RequestBody SysBackground backgroundData) {
        try {
            background.save(backgroundData);
            return ResultFactory.buildSuccessResult(backgroundData);
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e+"");
        }

    }

    //base64字符串转化成图片
    @PostMapping("/upload")
    @ResponseBody
    public Result GenerateImage(@RequestBody List<Map<String, String>> imgBase64) {
        //对字节数组字符串进行Base64解码并生成图片
        if (imgBase64 == null) //图像数据为空
            return ResultFactory.buildFailResult("提交数据为空");
        BASE64Decoder decoder = new BASE64Decoder();

        String realPath = ClassUtils.getDefaultClassLoader().getResource("").getPath().substring(1);
        String path = realPath + "static/pptImg/bgImg/";
        try {
            //Base64解码
            for (Map<String, String> map : imgBase64) {
                String data = map.get("data");
                data = data.substring(data.indexOf(",") + 1);
                byte[] b = decoder.decodeBuffer(data);
                for (int i = 0; i < b.length; ++i) {
                    if (b[i] < 0) {//调整异常数据
                        b[i] += 256;
                    }
                }
                //生成jpeg图片

                String imgFilePath = path + map.get("name");//新生成的图片
                OutputStream out = new FileOutputStream(imgFilePath);
                out.write(b);
                out.flush();
                out.close();

            }
            return ResultFactory.buildSuccessResult(imgBase64);
        } catch (Exception e) {
            return ResultFactory.buildFailResult("发送异常：" + e.toString());
        }
    }

}
