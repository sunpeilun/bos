package com.czxy.bos.service.transit;

import com.czxy.bos.dao.take_delivery.WayBillMapper;
import com.czxy.bos.dao.transit.SignMapper;
import com.czxy.bos.dao.transit.TransitInfoMapper;
import com.czxy.bos.domain.take_delivery.WayBill;
import com.czxy.bos.domain.transit.SignInfo;
import com.czxy.bos.domain.transit.TransitInfo;
import com.czxy.es.dao.WayBillRepository;
import com.czxy.es.pojo.ESWayBill;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SignService {

    @Autowired
    private SignMapper signMapper;

    @Autowired
    private TransitInfoMapper transitInfoMapper;

    @Autowired
    private WayBillMapper wayBillMapper;

    @Autowired
    private WayBillRepository wayBillRepository;


    public void saveSign(SignInfo signInfo){
        //1 保存当前签收数据
        signMapper.insert(signInfo);
        //2 修改TransitInfo的状态
        TransitInfo transitInfo = transitInfoMapper.selectByPrimaryKey(signInfo.getId());
        transitInfo.setStatus("已签收");
        transitInfoMapper.updateByPrimaryKey(transitInfo);
        //3 修改wayBill的状态
        WayBill wayBill = wayBillMapper.selectByPrimaryKey(transitInfo.getWayBillId());
        wayBill.setSignStatus(4);
        wayBillMapper.updateByPrimaryKey(wayBill);

        ESWayBill esWayBill = new ESWayBill();
        BeanUtils.copyProperties(wayBill,esWayBill);
        wayBillRepository.save(esWayBill);


    }


}
