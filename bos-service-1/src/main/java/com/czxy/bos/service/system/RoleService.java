package com.czxy.bos.service.system;

import com.czxy.bos.dao.system.RoleMapper;
import com.czxy.bos.domain.system.Role;
import com.czxy.bos.domain.system.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;


    public List<Role> findRoleByUser(User user){
        //如果用户是管理员，直接查所有
        if(user.getUsername().equals("admin")){
            return roleMapper.selectAll();
        }
        //如果用户不是管理员，就查找用户拥有的角色
        List<Role> roleList = roleMapper.findRoleByUser(user.getId());
        return roleList;
    }



}
