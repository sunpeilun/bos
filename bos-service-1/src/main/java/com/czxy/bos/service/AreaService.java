package com.czxy.bos.service;

import com.czxy.bos.dao.AreaMapper;
import com.czxy.bos.domain.base.Area;
import com.czxy.bos.domain.vo.DataGridResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AreaService {

    @Autowired
    private AreaMapper areaMapper;



    public void saveAreas(List<Area> list){
        for(Area a:list){
            areaMapper.insert(a);
        }
    }

    public List<Area> findAllAreas(){
        return areaMapper.selectAll();
    }

    public DataGridResult findAreaByPage(Integer page, Integer rows, Area area){
        // 分页组件
        PageHelper.startPage(page,rows);

        // 添加条件

        // 查询
        List<Area> areas = areaMapper.select(null);
        // 封装分页信息
        PageInfo<Area> pageInfo = new PageInfo<>(areas);
        // 封装返回结果
        DataGridResult result = new DataGridResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(pageInfo.getList());
        return result;
    }

}
