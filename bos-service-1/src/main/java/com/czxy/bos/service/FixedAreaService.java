package com.czxy.bos.service;

import com.czxy.bos.dao.CourierMapper;
import com.czxy.bos.dao.FixedAreaCourierMapper;
import com.czxy.bos.dao.FixedAreaMapper;
import com.czxy.bos.domain.base.Courier;
import com.czxy.bos.domain.base.FixedArea;
import com.czxy.bos.domain.base.FixedAreaCourier;
import com.czxy.bos.domain.vo.DataGridResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FixedAreaService {

    @Autowired
    private FixedAreaMapper fixedAreaMapper;

    public void addFixedArea(FixedArea fixedArea){
        fixedAreaMapper.insert(fixedArea);
    }


    public DataGridResult findFixedAreaByPage(Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        List<FixedArea> list = fixedAreaMapper.select(null);
        PageInfo<FixedArea> info = new PageInfo<>(list);
        DataGridResult result = new DataGridResult();
        result.setTotal(info.getTotal());
        result.setRows(info.getList());
        return result;
    }
    @Autowired
    private CourierMapper courierMapper;

    @Autowired
    private FixedAreaCourierMapper fixedAreaCourierMapper;

    public void assocationCourier(String fixedAreaId,Integer taketimeId,Integer courierId){
        // 快递员关联时间
        Courier courier = courierMapper.selectByPrimaryKey(courierId);
        courier.setTaketimeId(taketimeId);
        // 提交数据库修改
        courierMapper.updateByPrimaryKey(courier);
        // 定区关联快递员
        FixedAreaCourier fixedAreaCourier = new FixedAreaCourier();
        fixedAreaCourier.setFixedAreaId(fixedAreaId);
        fixedAreaCourier.setCourierId(courierId);

        fixedAreaCourierMapper.insert(fixedAreaCourier);
    }






}
