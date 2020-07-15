package com.sunlee.utils;

import com.sunlee.SpringMywordApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.InetAddress;

/**
 * 获取当前项目的访问路径
 *
 * @author sunlee
 * @date 2020/4/22 11:10
 */
@Controller
public class AppUrlUtils {

    private static ConfigurableApplicationContext ctx;

    public AppUrlUtils(ConfigurableApplicationContext ctx) {
        this.ctx = ctx;
    }
    @RequestMapping("/testcurrentapi")
    @ResponseBody
    public static String getAppUrl() {
        String host="localhost";
        int port=8080;
        String contextPath="/pptapi";
        try {
            host = InetAddress.getLocalHost().getHostAddress();
            TomcatServletWebServerFactory tomcatServletWebServerFactory = (TomcatServletWebServerFactory) ctx.getBean("tomcatServletWebServerFactory");
            contextPath = tomcatServletWebServerFactory.getContextPath();
            port = tomcatServletWebServerFactory.getPort();

            System.out.println("http://" + host + ":" + port + contextPath);

            return "http://" + host + ":" + port + contextPath;
        } catch (Exception e) {
            e.printStackTrace();
            return "http://" + host + ":" + port + contextPath;
        }

    }
}
