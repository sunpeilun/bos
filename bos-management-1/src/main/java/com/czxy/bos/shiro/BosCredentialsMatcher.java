package com.czxy.bos.shiro;

import com.czxy.bos.util.Encrypt;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * 密码比较器
 *
 *
 */
public class BosCredentialsMatcher extends SimpleCredentialsMatcher {

    /**
     * 重写该方法
     * @param token:用户页面输入的信息
     * @param info:数据库中的信息
     * @return
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //1 获取用户页面输入的密码--加密
        //1.1 强转
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        //1.2 获取用户页面的密码
        char[] pwd = upToken.getPassword();
        //1.3 char数组转String
        String myPwd = new String(pwd);
        //1.4 加密
        String newPwd = Encrypt.md5(myPwd, upToken.getUsername());
        //2 获取数据库中的密码
        Object dbPwd = info.getCredentials();
        //3 比对
        return equals(newPwd,dbPwd);
    }
}
