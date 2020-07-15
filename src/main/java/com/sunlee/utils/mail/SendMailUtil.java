package com.sunlee.utils.mail;

import com.sunlee.entity.User;
import com.sunlee.repository.UserRepository;
import com.sunlee.utils.AppUrlUtils;
import com.sunlee.utils.MD5Utils;
import com.sunlee.utils.result.Result;
import com.sunlee.utils.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author sunlee
 * @date 2020/4/7 11:22
 */

public class SendMailUtil {

    @Autowired
    UserRepository userRepository;

    /**
     * 发送邮箱验证
     * 返回一个验证码
     *
     * @param toMail 接收的邮箱
     */
    public static String toSend(String toMail) {

        MailOperation operation = new MailOperation();
        String user = "15627915168@163.com";
        String password = "AOLEFSBMCLCCSFGX";
        String host = "smtp.163.com";
        String from = "15627915168@163.com";
        String subject = "注册好多PPT账号";
        //邮箱内容
        StringBuffer sb = new StringBuffer();
//        String yzm = RandomUtil.getRandomString(6);
        String yzm = String.valueOf((100000 + (int) (Math.random() * 999999)));
        sb.append("<!DOCTYPE>" + "<div bgcolor='#f1fcfa'   style='border:1px solid #d9f4ee; font-size:14px; line-height:22px; color:#005aa0;padding-left:1px;padding-top:5px;   padding-bottom:5px;'><span style='font-weight:bold;'>温馨提示：</span>"
                + "<div style='width:950px;font-family:arial;'>欢迎注册好多PPT账号，您的注册码为：<br/><h2 style='color:green'>" + yzm + "</h2><br/>本邮件由系统自动发出，请勿回复。<br/>感谢您的使用。<br/>好多PPT网</div>"
                + "</div>");
        try {
            operation.sendMail(user, password, host, from, toMail,
                    subject, sb.toString());
            return yzm;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "发送验证码失败！";
        }
    }

    /**
     * 重置密码
     * @param user
     * @return message
     */
    public Result toSendUrl(User user) {
        try {


            String myMd5 = user.getUsername() + user.getPassword();
            myMd5 = MD5Utils.getSaltMD5(myMd5);
            MailOperation operation = new MailOperation();
            String email = "15627915168@163.com";
            String password = "AOLEFSBMCLCCSFGX";
            String host = "smtp.163.com";
            String from = "15627915168@163.com";
            String subject = "好多PPT网密码重置";
            //邮箱内容
            StringBuffer sb = new StringBuffer();

            String resetPass = AppUrlUtils.getAppUrl() + "/forgetPassword/" + user.getUsername()+ "/" + myMd5;
            sb.append("<!DOCTYPE>" + "<div bgcolor='#f1fcfa'   style='border:1px solid #d9f4ee; font-size:14px; line-height:22px; color:#005aa0;padding-left:1px;padding-top:5px;   padding-bottom:5px;'><span style='font-weight:bold;'>温馨提示：</span>"
                    + "<div style='width:950px;font-family:arial;'>当前消息是好多PPT网重置密码的链接，若您未进行过任何操作，请务必忽视该条信息。您的密码重置链接为：<br/><h2 style='color:blue'>" + resetPass + "</h2><br/>本邮件由系统自动发出，请勿回复。<br/>感谢您的使用。<br/>好多PPT网</div>"
                    + "</div>");

            operation.sendMail(email, password, host, from, user.getUsername(),
                    subject, sb.toString());
            return ResultFactory.buildSuccessResult("重置链接发送成功，请尽快登陆邮箱重置密码！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultFactory.buildFailResult("重置链接发送失败");
        }
    }
}
