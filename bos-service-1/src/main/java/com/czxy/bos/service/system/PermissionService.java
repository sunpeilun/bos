package com.czxy.bos.service.system;

import com.czxy.bos.dao.system.PermissionMapper;
import com.czxy.bos.domain.system.Permission;
import com.czxy.bos.domain.system.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    public List<Permission> findPermissionByUser(User user){
        //如果是管理员，查找所有
        if(user.getUsername().equals("admin")){
            return permissionMapper.selectAll();
        }
        //如果不是管理员，根据权限查询
        return permissionMapper.findPermissionByUser(user.getId());
    }




}
