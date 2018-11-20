package com.czxy.bos.service.transit;

import com.czxy.bos.dao.take_delivery.WayBillMapper;
import com.czxy.bos.dao.transit.DeliveryInfoMapper;
import com.czxy.bos.dao.transit.TransitInfoMapper;
import com.czxy.bos.domain.take_delivery.WayBill;
import com.czxy.bos.domain.transit.DeliveryInfo;
import com.czxy.bos.domain.transit.TransitInfo;
import com.czxy.es.dao.WayBillRepository;
import com.czxy.es.pojo.ESWayBill;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeliveryService {

    @Autowired
    private DeliveryInfoMapper deliveryInfoMapper;

    @Autowired
    private TransitInfoMapper transitInfoMapper;


    @Autowired
    private WayBillMapper wayBillMapper;

    @Autowired
    private WayBillRepository wayBillRepository;

    /**
     *
     *
     *
     *
     * @param deliveryInfo
     */
    public void saveDelivery(DeliveryInfo deliveryInfo){
        // 1 保 存当前DeliveryService的信息
        deliveryInfoMapper.insert(deliveryInfo);
        // 2 TransitInfo的状态需要改成 “开始派件” 吗？
        TransitInfo transitInfo = transitInfoMapper.selectByPrimaryKey(deliveryInfo.getId());
        transitInfo.setStatus("开始派件");// --- 只有开始配送(还没有指定快递员)的时候，才可以开始派件(已经指定快递员)
        transitInfoMapper.updateByPrimaryKey(transitInfo);
        // 3 WayBill的状态需要改吗--mysql   +    es
        WayBill wayBill = wayBillMapper.selectByPrimaryKey(transitInfo.getWayBillId());
        wayBill.setSignStatus(3);
        wayBillMapper.updateByPrimaryKey(wayBill);

        ESWayBill esWayBill = new ESWayBill();
        BeanUtils.copyProperties(wayBill,esWayBill);
        wayBillRepository.save(esWayBill);

        // 4 派件只需要派一次，派件之后 TransitInfo的状态要改成 签收----省略


    }

}
