package com.czxy.bos.controller.system;

import com.czxy.bos.domain.system.Menu;
import com.czxy.bos.domain.system.User;
import com.czxy.bos.domain.vo.DataGridResult;
import com.czxy.bos.service.system.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private HttpSession session;


    @GetMapping("/list")
    public ResponseEntity<List<Menu>> findAll(){
        // 根据用户信息查找menu
        User user = (User) session.getAttribute("user");

        List<Menu> menuList = menuService.findMenuByUser(user);


        return new ResponseEntity<>(menuList,HttpStatus.OK);

    }


    @GetMapping
    public ResponseEntity<DataGridResult> findMenuByPage(Integer page,Integer rows){
        try {
            DataGridResult result = menuService.findMenuByPage(page, rows);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }





}
