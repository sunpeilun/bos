package com.czxy.bos.service;

import com.czxy.bos.dao.StandardMapper;
import com.czxy.bos.domain.base.Standard;
import com.czxy.bos.domain.vo.DataGridResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class StandardService {

    @Resource
    private StandardMapper standardMapper;


    public void addStandard(Standard standard){
        standardMapper.insert(standard);
    }


    public DataGridResult findStandardByPage(Integer page,Integer rows){
        PageHelper.startPage(page,rows);
        List<Standard> standards = standardMapper.select(null);
        PageInfo<Standard> info = new PageInfo<>(standards);
        DataGridResult result = new DataGridResult();
        result.setTotal(info.getTotal());
        result.setRows(info.getList());
        return result;
    }


    public List<Standard> findAllStandards() {
        return standardMapper.selectAll();
    }
}
