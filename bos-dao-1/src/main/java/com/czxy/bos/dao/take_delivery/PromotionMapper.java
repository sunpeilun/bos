package com.czxy.bos.dao.take_delivery;

import com.czxy.bos.domain.take_delivery.Promotion;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface PromotionMapper extends Mapper<Promotion> {

    @Update("update t_promotion set status = '2' where END_DATE < now() and status = '1'")
    public void changeStatus();


}
