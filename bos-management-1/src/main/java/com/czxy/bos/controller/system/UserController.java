package com.czxy.bos.controller.system;

import com.czxy.bos.domain.system.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private HttpSession session;

    @GetMapping("/login")
    public ResponseEntity<Void> login(User user){
        //1 接收页面参数，转成对象----系统以及自动完成了

        //2 获取Subject对象
        Subject subject = SecurityUtils.getSubject();
        //3 Subject启动Shiro
        // 准备数据
        UsernamePasswordToken upToken = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        subject.login(upToken);


        // 能够执行到第八步，肯定登录已经成功
        //8 获取用户信息，保存到session中
        User loginUser = (User) subject.getPrincipal();
        // 放入session中
        session.setAttribute("user",loginUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(){
        //移除session中的登录信息
        session.removeAttribute("user");
        //移除shiro中的登录信息
        SecurityUtils.getSubject().logout();

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("isLogin")
    public ResponseEntity<Void> isLogin(){
        User user = (User) session.getAttribute("user");
        if(user==null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);

    }


}
