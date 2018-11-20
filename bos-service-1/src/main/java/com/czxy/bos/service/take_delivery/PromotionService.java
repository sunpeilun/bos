package com.czxy.bos.service.take_delivery;

import com.czxy.bos.dao.take_delivery.PromotionMapper;
import com.czxy.bos.domain.take_delivery.Promotion;
import com.czxy.bos.domain.vo.DataGridResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PromotionService {

    @Autowired
    private PromotionMapper promotionMapper;

    public void savePromotion(Promotion promotion){
        promotionMapper.insert(promotion);
    }



    public DataGridResult findPromotionByPage(Integer page,Integer rows){
        // 调用分页组件
        PageHelper.startPage(page,rows);
        // 查找数据
        List<Promotion> list = promotionMapper.select(null);
        // 封装结果
        PageInfo<Promotion> info = new PageInfo<>(list);
        // 封装返回结果
        DataGridResult result = new DataGridResult();
        result.setTotal(info.getTotal());
        result.setRows(info.getList());
        return result;
    }


    public void changeStatus(){
        promotionMapper.changeStatus();
    }


}
