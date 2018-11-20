package com.czxy.bos.service;

import com.czxy.bos.dao.CourierMapper;
import com.czxy.bos.dao.StandardMapper;
import com.czxy.bos.domain.base.Courier;
import com.czxy.bos.domain.vo.DataGridResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class CourierService {

    @Autowired
    private CourierMapper courierMapper;


    public void addCourier(Courier courier){
        courierMapper.insert(courier);
    }


    public DataGridResult findCourierByPage(Integer page,Integer rows){
        PageHelper.startPage(page,rows);// PageHelper 对紧跟着它的第一条sql语句进行分页操作
//        List<Courier> list = courierMapper.select(null); // 不再使用这条语句
        List<Courier> list = courierMapper.findAllCouriers();
        PageInfo<Courier> info = new PageInfo<>(list);
        DataGridResult result = new DataGridResult();
        result.setTotal(info.getTotal());
        result.setRows(info.getList());
        return result;
    }


    public void deleteCouriers(String idStr) {
        String[] ids = idStr.split(",");
        for(String id:ids){
            // 假删除--->修改--->先查再改
            Courier courier = courierMapper.selectByPrimaryKey(Integer.parseInt(id));
            courier.setDeltag("0");
            // 修改数据库
            courierMapper.updateByPrimaryKey(courier);
        }


    }

    @Autowired
    private StandardMapper standardMapper;

    public DataGridResult findCourierByPageAndCondition(Integer page, Integer rows, Courier courier) {
        PageHelper.startPage(page,rows);
        // 创建Example
        Example example = new Example(Courier.class);
        Example.Criteria criteria = example.createCriteria();

        // 条件判断 ,如何判断非空 StringUtils
        if(StringUtils.isNotBlank(courier.getCourierNum())){
            criteria.andLike("courierNum","%"+courier.getCourierNum()+"%");
        }
        // standard.id
        if(courier.getStandard()!=null){
            if(courier.getStandard().getId()!=null){
                criteria.andEqualTo("standardId",courier.getStandard().getId());
            }
        }
        // company
        if(StringUtils.isNotBlank(courier.getCompany())){
            criteria.andLike("company","%"+courier.getCompany()+"%");
        }
        // type
        if(StringUtils.isNotBlank(courier.getType())){
            criteria.andLike("type","%"+courier.getType()+"%");
        }

        // 查询
        List<Courier> list = courierMapper.selectByExample(example);
        for(Courier c:list){
            c.setStandard(standardMapper.selectByPrimaryKey(c.getStandardId()));
        }

        PageInfo<Courier> info = new PageInfo<>(list);
        DataGridResult result = new DataGridResult();
        result.setTotal(info.getTotal());
        result.setRows(info.getList());
        return result;

    }


    /**
     * 查找未安排工作的快递员信息
     *
     */
    public List<Courier> findNoAssociationCouriers(){
        List<Courier> list = courierMapper.findNoAssociationCouriers();
        return list;
    }


    /**
     * 查找与xxx定区关联的快递员信息
     * @return
     */
    public List<Courier> findAssociationCouriers(String fixedAreaId){
        List<Courier> list = courierMapper.findAssociationCouriers(fixedAreaId);
        return list;
    }









}
