import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationListener;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author sunlee
 * @date 2020/6/7 16:33
 */
@SpringBootTest
public class SBTest {

    @LocalServerPort
    private String port ;

    @Test
    public void getIp() throws UnknownHostException {
        InetAddress localHost;
        localHost = Inet4Address.getLocalHost();
        String ip = localHost.getHostAddress();  // 返回格式为：xxx.xxx.xxx
        System.out.println(ip);

        System.out.println(port);

//        System.out.println( serverConfig.getUrl());

    }



}
