package com.sunlee;


import com.sunlee.utils.AppUrlUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringMywordApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(SpringMywordApplication.class, args);
       new AppUrlUtils(ctx);
    }
    // 不重写打包war部署到tomcat接口会报404
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringMywordApplication.class);
    }

}
