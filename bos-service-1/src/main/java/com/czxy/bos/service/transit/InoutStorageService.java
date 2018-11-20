package com.czxy.bos.service.transit;

import com.czxy.bos.dao.transit.InoutStorageMapper;
import com.czxy.bos.dao.transit.TransitInfoMapper;
import com.czxy.bos.domain.transit.InOutStorageInfo;
import com.czxy.bos.domain.transit.TransitInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InoutStorageService {

    @Autowired
    private InoutStorageMapper inoutStorageMapper;

    @Autowired
    private TransitInfoMapper transitInfoMapper;


    /**
     * 保存出入库数据？业务是什么?
     * 1 必须要现有入库，才可以有出库
     * 2 如果地点与目的地一致，就自动进入派单状态
     * 3 如果地点与目的地不一致，则继续出入库操作
     *
     * @param inOutStorageInfo
     */
    public String saveInoutStorage(InOutStorageInfo inOutStorageInfo){
        //1 如果当前 TransitInfo在InOutStorage表中  没有  记录的时候，只可以入库 ----根据transitInfoId去InOutStorage表中查数据
        List<InOutStorageInfo> list = inoutStorageMapper.findInOutStorageInfoByTransitInfoId(inOutStorageInfo.getTransitInfoId());
        if(list.size()==0){
            if(inOutStorageInfo.getOperation().equals("入库")){
                // 如果地点与目的地一致，就自动进入派单状态
                // 获取TransitInfo的信息
                TransitInfo transitInfo = transitInfoMapper.selectByPrimaryKey(inOutStorageInfo.getTransitInfoId());
                if(inOutStorageInfo.getAddress().contains(transitInfo.getOutletAddress())){
                    // 自动进入派单
                    transitInfo.setStatus("开始配送");
                    transitInfoMapper.updateByPrimaryKey(transitInfo);
                }
                // 只需要将该数据插入数据库即可
                inoutStorageMapper.insert(inOutStorageInfo);
                return "新增成功";
            }else{
                return "当前只能执行入库操作";
            }
        }
        //2 如果当前  TransitInfo在InOutStorage表中  有  记录的时候，判断上一次操作是入库还是出库
        //  上一次操作是入库的话，这一次只能是出库，上一次操作是出库的话，这一次操作只能是入库

        if (list.get(list.size()-1).getOperation().equals(inOutStorageInfo.getOperation())) {
            return "两次操作都是"+inOutStorageInfo.getOperation()+"，系统无法成功插入数据";
        }


        // 如果地点与目的地一致，就自动进入派单状态
        // 获取TransitInfo的信息
        TransitInfo transitInfo = transitInfoMapper.selectByPrimaryKey(inOutStorageInfo.getTransitInfoId());
        if(inOutStorageInfo.getAddress().contains(transitInfo.getOutletAddress())){
            // 自动进入派单
            transitInfo.setStatus("开始配送");
            transitInfoMapper.updateByPrimaryKey(transitInfo);
        }
        // 只需要将该数据插入数据库即可
        inoutStorageMapper.insert(inOutStorageInfo);
        return "新增成功";
    }


}
