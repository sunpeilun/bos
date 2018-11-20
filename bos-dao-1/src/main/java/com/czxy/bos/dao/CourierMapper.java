package com.czxy.bos.dao;

import com.czxy.bos.domain.base.Courier;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface CourierMapper extends Mapper<Courier> {

    @Select("select * from t_courier")
    @Results({
        @Result(property = "id",column = "id"),
        @Result(property = "standard" , column = "standard_id",
            one = @One(select = "com.czxy.bos.dao.StandardMapper.selectByPrimaryKey")
        )
    })
    public List<Courier> findAllCouriers();

    @Select("select * from t_courier where id not in (select courier_id from t_fixedarea_courier)")
    public List<Courier> findNoAssociationCouriers();

    @Select("select * from t_courier where id in (select DISTINCT(courier_id) from t_fixedarea_courier where FIXED_AREA_ID = #{fixedAreaId})")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "courierNum",column = "courier_num"),
            @Result(property = "standard" ,column = "standard_id",
                one = @One(select = "com.czxy.bos.dao.StandardMapper.selectByPrimaryKey")
            ),
            @Result(property = "taketime",column = "taketime_id",
                one = @One(select = "com.czxy.bos.dao.TakeTimeMapper.selectByPrimaryKey")
            )
    })
    public List<Courier> findAssociationCouriers(String fixedAreaId);


}
