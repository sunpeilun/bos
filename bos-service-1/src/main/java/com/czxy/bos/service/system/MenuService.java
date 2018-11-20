package com.czxy.bos.service.system;

import com.czxy.bos.dao.system.MenuMapper;
import com.czxy.bos.domain.system.Menu;
import com.czxy.bos.domain.system.User;
import com.czxy.bos.domain.vo.DataGridResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuService {

    @Autowired
    private MenuMapper menuMapper;

    public List<Menu> findMenuByUser(User user){
        return menuMapper.findMenuByUser(user.getId());
    }


    public DataGridResult findMenuByPage(Integer page,Integer rows){
        // 分页组件
        PageHelper.startPage(page,rows);
        // 查找
        List<Menu> list = menuMapper.select(null);
        // 封装list
        PageInfo<Menu> info = new PageInfo<>(list);
        // 封装DataGrid需要的结果
        DataGridResult result = new DataGridResult();
        result.setTotal(info.getTotal());
        result.setRows(info.getList());

        return result;

    }






}
