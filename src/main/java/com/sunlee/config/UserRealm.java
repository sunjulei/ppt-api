package com.sunlee.config;

import com.sunlee.entity.User;
import com.sunlee.repository.UserRepository;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author sunlee
 * @date 2020/3/1 9:44
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserRepository userRepository;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了授权");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        info.addStringPermission("user:add");

        Subject subject = SecurityUtils.getSubject();
        User currentUser = (User) subject.getPrincipal();//拿到User对象
        info.addStringPermission(currentUser.getPerms());//增加权限

        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        System.out.println("执行了认证");

        UsernamePasswordToken userToken=(UsernamePasswordToken)token;
        //连接真实数据库
        User user = userRepository.queryUserByName(userToken.getUsername());

        if (user==null){//没有这个人
            return null;//UnknownAccountException
        }


        return new SimpleAuthenticationInfo(user,user.getPassword(),"");
    }
}
