package com.sunlee.controller;

import com.sunlee.constant.LoginConstant;
import com.sunlee.entity.CheckYzm;
import com.sunlee.entity.User;
import com.sunlee.repository.CheckYzmRepository;
import com.sunlee.repository.UserRepository;
import com.sunlee.utils.MD5Utils;
import com.sunlee.utils.mail.SendMailUtil;
import com.sunlee.utils.result.Result;
import com.sunlee.utils.result.ResultFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;
import sun.misc.BASE64Decoder;
import sun.security.provider.MD5;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author sunlee
 * @date 2020/3/1 10:15
 */
@RestController
public class UserController {


    @Autowired
    UserRepository userRepository;

    @Autowired
    CheckYzmRepository checkYzmRepository;

    @RequestMapping("/findByName/{name}")
    User findById(@PathVariable String name) {
//        System.out.println("findByName:" + name);
        return userRepository.queryUserByName(name);
    }

    @GetMapping(value = "/user/findAll/{page}/{size}")
    public Page<User> findAll(@PathVariable Integer page, @PathVariable Integer size) {
        PageRequest request = PageRequest.of(page, size);
        return userRepository.findAll(request);
    }

    @GetMapping("/toLogin")
    public Result toLogin() {
        String message = "未登录";
        return ResultFactory.buildFailResult(message);
    }

    @PostMapping(value = "/login")
    public Result login(@RequestBody User user) {

        // 对 html 标签进行转义，防止 XSS 攻击
        String username = HtmlUtils.htmlEscape(user.getUsername());
        //获取当前的用户
        Subject subject = SecurityUtils.getSubject();
        //封装用户的登录数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, user.getPassword());


        try {
            subject.login(token);
            subject.isRemembered();
            return ResultFactory.buildSuccessResult(token);
        } catch (AuthenticationException e) {
            String message = "账号密码错误";
            return ResultFactory.buildFailResult(message);
//            return LoginConstant.LOGIN_OTHER_ERR;
        }
    }

    @RequestMapping("/noauth")
    public String unauthorized() {
        return LoginConstant.AUTH_NO;
    }

    @ResponseBody
    @GetMapping("/logout")
    public Result logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        String message = "成功登出";
        return ResultFactory.buildSuccessResult(message);
    }

    @PostMapping("/register/{yzm}")
    @ResponseBody
    public Result register(@RequestBody User user, @PathVariable("yzm") String yzm) {

        String username = user.getUsername();
        username = HtmlUtils.htmlEscape(username);
        user.setUsername(username);

        User getUser = userRepository.queryUserByName(username);
        boolean exist = getUser != null;

        if (exist) {
            String message = "该账号已被注册！！";
            return ResultFactory.buildFailResult(message);
        }

        CheckYzm check = checkYzmRepository.queryUser(username, yzm);
        boolean yzmExist = check != null;

        if (!yzmExist) {
            return ResultFactory.buildFailResult("验证码错误！");
        }
        user.setRegisterTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        userRepository.save(user);
        return ResultFactory.buildSuccessResult(user);
    }

    @PostMapping("/register")
    public Result registerOp(@RequestBody User user) {

        String username = user.getUsername();
        username = HtmlUtils.htmlEscape(username);
        user.setUsername(username);

        User getUser = userRepository.queryUserByName(username);
        boolean exist = getUser != null;

        if (exist) {
            String message = "该账号已被注册！！";
            return ResultFactory.buildFailResult(message);
        }

        user.setRegisterTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        userRepository.save(user);
        return ResultFactory.buildSuccessResult(user);
    }

    @PutMapping("/user/update")
    public Result update(@RequestBody User user) {

        try {
            userRepository.save(user);
            return ResultFactory.buildSuccessResult(user);
        } catch (Exception e) {
            return ResultFactory.buildFailResult("账号已存在");
        }

    }

    @DeleteMapping("/user/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        try {
            Optional<User> user = userRepository.findById(id);

            String realPath = ClassUtils.getDefaultClassLoader().getResource("").getPath().substring(1);
            String path = realPath + "static" + user.get().getHeadImg();
            File file = new File(path);
            if (file.exists()) {//文件是否存在
                //删除
                file.delete();
            }
            userRepository.deleteById(id);
            return ResultFactory.buildSuccessResult(id);
        } catch (Exception e) {
            return ResultFactory.buildFailResult("账号删除发生异常");
        }

    }

    @PostMapping("/user/deleteAll")
    public Result deleteAll(@RequestBody List<User> userList) {
        try {
            for (int i = 0; i < userList.size(); i++) {
                deleteById(userList.get(i).getId());
            }
//            userRepository.deleteAll(userList);
            return ResultFactory.buildSuccessResult(userList);
        } catch (Exception e) {
            return ResultFactory.buildFailResult("账号删除发生异常");
        }
    }

    //base64字符串转化成图片
    @PostMapping("/user/uploadHeadImg")
    @ResponseBody
    public Result GenerateImage(@RequestBody List<Map<String, String>> imgBase64) {
        //对字节数组字符串进行Base64解码并生成图片
        if (imgBase64 == null) //图像数据为空
            return ResultFactory.buildFailResult("提交数据为空");
        BASE64Decoder decoder = new BASE64Decoder();

        String realPath = ClassUtils.getDefaultClassLoader().getResource("").getPath().substring(1);
        String path = realPath + "static/headImg/";
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


    @PostMapping("/user/yzm")
    public Result yzm(@RequestBody CheckYzm reqCheck) {
        String mail = reqCheck.getUsername();
        mail = HtmlUtils.htmlEscape(mail);

        User getUser = userRepository.queryUserByName(mail);
        boolean exist = getUser != null;

        if (exist) {
            return ResultFactory.buildFailResult("该账号已被注册！！");
        }

        CheckYzm checkYzm = new CheckYzm();

        CheckYzm queryCheck = checkYzmRepository.queryUserByName(mail);

        if (queryCheck != null) {
            checkYzm.setId(queryCheck.getId());
        }

        String yzm = SendMailUtil.toSend(mail);
        if (yzm.length() != 6) {
            return ResultFactory.buildFailResult("验证码发送失败！");
        }


        checkYzm.setUsername(mail);
        checkYzm.setYzm(yzm);
        checkYzmRepository.save(checkYzm);

        return ResultFactory.buildSuccessResult(checkYzm);

    }


    @PostMapping("/user/forgetPassword")
    public Result forgetPasswordfa(@RequestBody User user) {
        User user1 = userRepository.queryUserByName(user.getUsername());
        if (user1 == null) {
            //用户不存在
            return ResultFactory.buildFailResult("用户不存在");
        }
        new SendMailUtil().toSendUrl(user1);
        return ResultFactory.buildSuccessResult("发送重置链接成功");
    }

    @GetMapping("/forgetPassword/{username}/{md5}")
    @ResponseBody
    public String forgetPassword(@PathVariable String username, @PathVariable String md5) {
        User user = userRepository.queryUserByName(username);

        if (user == null) {
            return "不存在该用户，链接已失效";
        }
        boolean isMd5 = MD5Utils.getSaltverifyMD5(user.getUsername() + user.getPassword(), md5);
        if (isMd5) {
            String randPass = String.valueOf((100000 + (int) (Math.random() * 999999)));
            user.setPassword(randPass);
            userRepository.save(user);
            return "好多PPT网：已将密码重置为：" + randPass + "。下次登录时，建议您再次手动修改密码。";
        } else {
            return "该链接无效";
        }
    }

    @GetMapping("/user/todayNum")
    public Result findTodayUserNum() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        List<User> list = userRepository.findTodayNum(date);

        return ResultFactory.buildSuccessResult(list.size());
    }
}
