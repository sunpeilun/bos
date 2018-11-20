package com.czxy.bos.shiro;

import com.czxy.bos.dao.system.PermissionMapper;
import com.czxy.bos.domain.system.Permission;
import com.czxy.bos.domain.system.Role;
import com.czxy.bos.domain.system.User;
import com.czxy.bos.service.system.PermissionService;
import com.czxy.bos.service.system.RoleService;
import com.czxy.bos.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 自定义Realm域
 *
 */
public class BosRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;


    /**
     * 参数Token：用户名和密码
     * 认证的实现思路：
     * 1 调用Service-----> 调用DAO -----> 只根据用户名查找用户信息
     *
     *
     *
     * @param token
     * @return
     * @throws AuthenticationException ： 认证之后的信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("认证");
        //1 获取用户页面输入的用户名
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        //2 调用Service查找用户信息
        User user = userService.findUserByUsername(username);
        //3 判断
        if(user==null){
            return  null;
        }
        //表明用户名是对了，得比较密码
        //Object principal, Object credentials, String realmName
        //第一个参数：principal 身份信息---->数据库中查出来的数据
        //第二个参数：credentials 凭证，密码
        //第三个参数：realmName Realm域的名字,当前类名，也可以随意
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user,user.getPassword(),this.getName());
        return authenticationInfo;
    }

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 需求1：区域管理必须拥有role角色
     *
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("授权");
        // 创建返回值信息
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        // 获取当前登录用户
//        User user = (User) principals.fromRealm(this.getName()).iterator().next();
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        // 获取到用户之后，调用Service
        List<Role> roleList = roleService.findRoleByUser(user);
        // 遍历RoleList
        for(Role r:roleList){
            // 将授权信息给shiro
            authorizationInfo.addRole(r.getKeyword());
        }

        /******************快递员设置需要xxx权限*********************/
        List<Permission> permissionList = permissionService.findPermissionByUser(user);
        // 给shiro
        for(Permission p:permissionList){
            authorizationInfo.addStringPermission(p.getKeyword());
        }

        return authorizationInfo;
    }


}
