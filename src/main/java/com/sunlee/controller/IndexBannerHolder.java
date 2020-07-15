package com.sunlee.controller;

import com.sunlee.entity.FileHistoryDown;
import com.sunlee.entity.IndexBanner;
import com.sunlee.entity.PptFile;
import com.sunlee.repository.IndexBannerRepository;
import com.sunlee.utils.result.Result;
import com.sunlee.utils.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author sunlee
 * @date 2020/2/28 23:13
 */
@RestController
@RequestMapping("/indexBanner")
public class IndexBannerHolder {

    @Autowired
    IndexBannerRepository banner;

    @GetMapping("/findAll/{page}/{size}")
    Page<IndexBanner> findAll(@PathVariable Integer page,@PathVariable Integer size){
        PageRequest request = PageRequest.of(page, size);
        return banner.findAll(request);
    }

    @GetMapping("/findById/{id}")
    Optional<IndexBanner> findById(@PathVariable Integer id){
        return banner.findById(id);
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {

        try {
            banner.deleteById(id);
            return ResultFactory.buildSuccessResult(id);
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e+"");
        }

    }

    @PostMapping("/deleteAll")
    public Result deleteAll(@RequestBody List<IndexBanner> fileList) {
        try {
            banner.deleteAll(fileList);
            return ResultFactory.buildSuccessResult(fileList);
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
        String path = realPath + "static/pptImg/banner/";
        File cuFile = new File(path);
        if (!cuFile.exists()) {
            cuFile.mkdirs();
        }
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

    @PostMapping("/update")
    public Result update(@RequestBody IndexBanner indexBanner) {
        try {
            System.out.println(indexBanner.getImgUrl()+"-------");
            banner.save(indexBanner);
            return ResultFactory.buildSuccessResult(indexBanner);
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e+"");
        }

    }

}
