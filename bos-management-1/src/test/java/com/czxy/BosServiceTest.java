package com.czxy;

import com.czxy.bos.dao.take_delivery.WayBillMapper;
import com.czxy.bos.domain.take_delivery.WayBill;
import com.czxy.es.dao.WayBillRepository;
import com.czxy.es.pojo.ESWayBill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BosManagement1Application.class)
public class BosServiceTest {

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private WayBillRepository wayBillRepository;

    @Autowired
    private WayBillMapper wayBillMapper;


    @Test
    public void pareData(){
        List<WayBill> list = wayBillMapper.selectAll();
        List<ESWayBill> esList = new ArrayList<>();

        // 将list中的数据拷贝到esList
        for(WayBill wb:list){
            ESWayBill eswb = new ESWayBill();
            // 将wb赋值给eswb
            // 只要属性的名字和类型一致，就自动赋值
            BeanUtils.copyProperties(wb,eswb);
            esList.add(eswb);
        }

        /**
         * 当保存数据的时候，会判断索引和类型是否已经创建，如果没有创建，则自动创建
         * 如果已经创建了，则直接插入数据
         */
        wayBillRepository.saveAll(esList);

    }





}
