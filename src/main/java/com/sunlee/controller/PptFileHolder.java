package com.sunlee.controller;

import com.sunlee.entity.PptFile;
import com.sunlee.entity.User;
import com.sunlee.repository.PptFileRepository;
import com.sunlee.repository.PptMenuRepository;
import com.sunlee.utils.SelectFileUtil;
import com.sunlee.utils.result.Result;
import com.sunlee.utils.result.ResultFactory;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author sunlee
 * @date 2020/2/16 10:10
 */
@RestController
@RequestMapping("/pptFile")
@AllArgsConstructor
@DynamicUpdate//插入使用默认值
@DynamicInsert//插入使用默认值
public class PptFileHolder {

    private final NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;

    @Autowired
    PptFileRepository pptFile;

    @Autowired
    PptMenuRepository pptMenu;

    //查找用户查询的文件
    @GetMapping("/findFileByMenuAndType/{menuId}/{typeName}/{page}/{size}")
    Page<PptFile> findFileByMenuAndType(@PathVariable("menuId") String menuId,
                                        @PathVariable("typeName") String type,
                                        @PathVariable("page") Integer page,
                                        @PathVariable("size") Integer size) {

        type = type.trim();
        String menu = "";
        if (!menuId.equals("0")) {
            menu = pptMenu.findMenuById(Integer.valueOf(menuId));
        }

        PageRequest request = PageRequest.of(page, size);

        if (menuId.equals("0")) {
            if (type.equals("全部")) {
                //选中菜单为全部，类型也是全部，查询所有菜单的文件
                return pptFile.findAll("通过", request);
            } else {
                //选中菜单为全部，类型为关键字搜索
                List<String> list = SelectFileUtil.strFromToList(type);
                return pptFile.findFileByTag("通过", list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), request);
            }
        } else if ("全部".equals(type) || "".equals(type)) {
            //选中菜单为非全部，但菜单下的类型为全部，查询该菜单下的所有文件
            return pptFile.findFileByMenu("通过", menu, request);
        } else if (menuId.equals("1") || menuId.equals("2") || menuId.equals("3")||menuId.equals("4") || menuId.equals("5") || menuId.equals("6")|| menuId.equals("7")|| menuId.equals("8")) {
            //选中菜单和类型都为非全部，而且是模板(1)、行业(2)、节日(3)、课件(7)、风格(8)
            List<String> list = SelectFileUtil.strFromToList(type);
            return pptFile.findFileByTag("通过", list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), request);
        }
        /*else if (menuId.equals("4") || menuId.equals("5") || menuId.equals("6")) {
            //选中菜单和类型都为非全部，而且是背景(4)、素材(5)、图表(6)
            return pptFile.findFileByMenuAndType("通过", menu, type, request);
        } */
        else {
            return null;
        }

    }

    //管理员查找所有
    @GetMapping("/findAll/{page}/{size}")
    Page<PptFile> findAll(@PathVariable("page") Integer page, @PathVariable("size") Integer size) {
        PageRequest request = PageRequest.of(page, size);
        return pptFile.findAll(request);
    }

    //用户查找自己的文件
    @GetMapping("/findAllByAuthor/{author}/{page}/{size}")
    Page<PptFile> findAllByAuthor(@PathVariable("author") String author,
                                  @PathVariable("page") Integer page,
                                  @PathVariable("size") Integer size) {
        PageRequest request = PageRequest.of(page, size);
        return pptFile.findAllByAuthor(author, request);
    }

    @GetMapping("/findFileById/{id}")
    Map<String, Object> findFileById(@PathVariable("id") Integer id) {
        List<PptFile> pptData = pptFile.findAllById(Collections.singleton(id));

        //把图片地址的字符串，转成对象，方便前端遍历
        String[] arr = pptData.get(0).getDetailImgUrl().split("\n");

        Map<String, Object> map = new HashMap<>();
        map.put("data", pptData);
        map.put("imgUrlList", arr);
        return map;
    }


    //实现Spring Boot 的文件下载功能
    @GetMapping("/download/{id}")
    public String downloadFile(@PathVariable("id") Integer id,
                               HttpServletRequest request,
                               HttpServletResponse response) throws UnsupportedEncodingException {

        String realPath = ClassUtils.getDefaultClassLoader().getResource("").getPath();

        String downUrl = "static" + pptFile.findById(id).get().getDownUrl();//下载的路径
        String fileName = pptFile.findById(id).get().getTitle() + downUrl.substring(downUrl.indexOf(".")); //下载的文件名

        String msg = null;
        // 如果文件名不为空，则进行下载
        if (fileName != null) {
            //设置文件路径
            File file = new File(realPath + downUrl);
            // 如果文件名存在，则进行下载
            if (file.exists()) {
                // 配置文件下载
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                // 下载文件能正常显示中文
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setHeader("Content-Length", "" + file.length());
                // 实现文件下载
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    msg = "success";
//                    System.out.println("Download the song successfully!");
                } catch (Exception e) {
                    msg = "fail";
//                    System.out.println("Download the song failed!");
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return msg;
    }


    /**
     * 预览视频文件, 支持 byte-range 请求
     */
    @GetMapping("/video/{url}")
    public void videoPreview(
            @PathVariable("url") String url,
            HttpServletRequest request, HttpServletResponse response) throws Exception {


        String realPath = ClassUtils.getDefaultClassLoader().getResource("").getPath().substring(1);


        String path = realPath + "static/pptVideo/" + url;
        Path filePath = Paths.get(path);

        if (Files.exists(filePath)) {
            String mimeType = Files.probeContentType(filePath);
            if (!StringUtils.isEmpty(mimeType)) {
                response.setContentType(mimeType);
            }
            request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
            nonStaticResourceHttpRequestHandler.handleRequest(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }

    /**
     * 首页推荐ppt文件
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/findRecom/{page}/{size}")
    Page<PptFile> findRecom(
            @PathVariable("page") Integer page,
            @PathVariable("size") Integer size) {
        PageRequest request = PageRequest.of(page, size);
        return pptFile.findRecom("通过", request);
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {

        try {

            Optional<PptFile> one = pptFile.findById(id);
            List<String> list = new ArrayList<>();

            String realPath = ClassUtils.getDefaultClassLoader().getResource("").getPath().substring(1);
            String path = realPath + "static";
            System.out.println(path);
            //封面图
            list.add(one.get().getRoughImgUrl());
            //详情图
            String[] split = one.get().getDetailImgUrl().split("\n");
            for (String s : split) {
                list.add(s);
            }
            //ppt文件
            list.add(one.get().getDownUrl());
            //视频
            list.add(one.get().getVideoUrl());

            for (int i = 0; i < list.size(); i++) {
                String url = list.get(i);
                System.out.println("s-----:" + url);
                File file = new File(path + url);
//                System.out.println(file.exists()+"存在-----:" + path + url);
                if (file.exists()) {//文件是否存在
                    //删除
                    file.delete();
                }
            }
            pptFile.deleteById(id);
            return ResultFactory.buildSuccessResult("删除成功");
        } catch (Exception e) {
            return ResultFactory.buildFailResult("找不到该文件");
        }

    }

    @PostMapping("/deleteAll")
    public Result deleteAll(@RequestBody List<PptFile> pptList) {
        try {
            for (int i = 0; i < pptList.size(); i++) {
                deleteById(pptList.get(i).getId());
            }
            pptFile.deleteAll(pptList);
            return ResultFactory.buildSuccessResult(pptList);
        } catch (Exception e) {
            return ResultFactory.buildFailResult("文件删除异常");
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
        String path = realPath + "static/pptImg/";
        try {
            File cuFile = new File(path);
            if (!cuFile.exists()) {
                cuFile.mkdirs();
            }
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

    @PutMapping("/update")
    public Result update(@RequestBody PptFile pptList) {
        try {
            pptFile.save(pptList);
            return ResultFactory.buildSuccessResult(pptList);
        } catch (Exception e) {
            return ResultFactory.buildFailResult("文件更新失败：" + e);
        }

    }

    @PostMapping("/add")
    public Result add(@RequestBody PptFile pptList) {
        try {
            Date date = new Date();
            String currTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
            pptList.setUpData(currTime);
            pptFile.save(pptList);
            return ResultFactory.buildSuccessResult(pptList);
        } catch (Exception e) {
            return ResultFactory.buildFailResult("文件上传失败：" + e);
        }

    }

    @PostMapping("/uploadFile")
    public Result daoru(@RequestParam("file") MultipartFile file, @RequestParam("fileName") String fileName) {

        //判断文件不为空
        if (!file.isEmpty()) {
            //获取文件类型 .ppt
            String type = file.getOriginalFilename();
            type = type.substring(type.lastIndexOf("."));

            if (!(".ppt".equals(type) || ".pptx".equals(type))) {
                System.out.println("上传文件格式错误");
                return ResultFactory.buildFailResult("上传文件格式错误");
            }
            String realPath = ClassUtils.getDefaultClassLoader().getResource("").getPath().substring(1);
            String path = realPath + "static/pptFile/";
            //指定本地存入路径
            try {
                File cuFile = new File(path);
                if (!cuFile.exists()) {
                    cuFile.mkdirs();
                }
                file.transferTo(Paths.get(path + fileName + type));
                System.out.println("上传文件成功");
            } catch (IOException e) {
                e.printStackTrace();
                return ResultFactory.buildFailResult("上传文件失败：" + e);
            }
        } else {
            System.out.println("文件是空的");
        }
        return ResultFactory.buildSuccessResult("上传文件成功");
    }

    @GetMapping("/todayNum")
    public Result findTodayNum() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        List<PptFile> list = pptFile.findTodayNum(date);

        return ResultFactory.buildSuccessResult(list.size());
    }

    @RequestMapping(value = "/uploadVideo", produces = "application/json;charset=UTF-8")
    public String uploadFile(@RequestParam("video") MultipartFile file, @RequestParam("videoName") String fileName) {

        //判断文件是否为空
        if (file.isEmpty()) {
            return "上传文件不可为空";
        }

        String realPath = ClassUtils.getDefaultClassLoader().getResource("").getPath().substring(1);
        String path = realPath + "static/pptVideo/" ;

        File file1 = new File(path);
        if (!file1.exists()){
            file1.mkdirs();
        }

        //创建文件路径
        File dest = new File(path+ fileName);

        try {
            //上传文件
            file.transferTo(dest); //保存文件
        } catch (IOException e) {
            return "上传失败";
        }

        return "上传成功";
    }
}
