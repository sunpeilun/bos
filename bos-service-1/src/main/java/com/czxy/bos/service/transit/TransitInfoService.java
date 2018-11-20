package com.czxy.bos.service.transit;

import com.alibaba.fastjson.JSON;
import com.czxy.bos.dao.take_delivery.WayBillMapper;
import com.czxy.bos.dao.transit.DeliveryInfoMapper;
import com.czxy.bos.dao.transit.InoutStorageMapper;
import com.czxy.bos.dao.transit.SignMapper;
import com.czxy.bos.dao.transit.TransitInfoMapper;
import com.czxy.bos.domain.map.BMapJsonResult;
import com.czxy.bos.domain.take_delivery.WayBill;
import com.czxy.bos.domain.transit.DeliveryInfo;
import com.czxy.bos.domain.transit.InOutStorageInfo;
import com.czxy.bos.domain.transit.SignInfo;
import com.czxy.bos.domain.transit.TransitInfo;
import com.czxy.bos.domain.vo.DataGridResult;
import com.czxy.es.dao.WayBillRepository;
import com.czxy.es.pojo.ESWayBill;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Transactional
public class TransitInfoService {

    @Autowired
    private TransitInfoMapper transitInfoMapper;

    @Autowired
    private WayBillMapper wayBillMapper;

    @Autowired
    private WayBillRepository wayBillRepository;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 新增运输配送信息
     *
     * @param wayBillIds
     */
    public void saveTransitInfo(String wayBillIds){
        //1 分割id
        String[] wayBillIdArray = wayBillIds.split(",");
        //2 遍历id
        for(String wayBillId:wayBillIdArray){
            // 先查再改
            WayBill wayBill = wayBillMapper.selectByPrimaryKey(wayBillId);
            //3 修改mysql中的waybill的signStatus
            wayBill.setSignStatus(2);
            wayBillMapper.updateByPrimaryKey(wayBill);
            //4 修改es中的wayBill的signStatus
            ESWayBill esWayBill = new ESWayBill();
            BeanUtils.copyProperties(wayBill,esWayBill);
            // 修改索引库
            wayBillRepository.save(esWayBill);
            //5 新增TransitInfo
            TransitInfo transitInfo = new TransitInfo();
            // id：自增长，系统自动完成


            // 设置outletAddress     上海     南京    北京     天津
            // 江苏省苏州市常熟市    常熟
            // 河北省张家口市尚义县   尚义      百度地图
            // 使用LBS云地理解析功能  发起HttpClient
            //
            String district = getDistrict(wayBill.getRecAddress());
            transitInfo.setOutletAddress(district);
            // 设置状态
            transitInfo.setStatus("出入库中转");
            //设置WayBillId
            transitInfo.setWayBillId(Integer.parseInt(wayBillId));

            // 调用Mapper保存数据
            transitInfoMapper.insert(transitInfo);

        }

    }


    public String getDistrict(String address){

        String url = "http://api.map.baidu.com/cloudgc/v1?address="+address+"&ak=nOXZem5adYQklK59hCYtECe3DbkkRWh5";
        // 通过restTemplate发起请求
        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
        // 获取返回结果
        String json = entity.getBody();
        // 通过fastjson将json字符串转成对象
        // 第一个参数：json字符串
        // 第二个参数：目标类型
        BMapJsonResult bMapJsonResult = JSON.parseObject(json, BMapJsonResult.class);

        String district = bMapJsonResult.getResult()[0].getAddress_components().getDistrict();

        return district;
    }


    @Autowired
    private InoutStorageMapper inoutStorageMapper;

    @Autowired
    private DeliveryInfoMapper deliveryInfoMapper;

    @Autowired
    private SignMapper signMapper;

    public DataGridResult findTransitInfoByPage(Integer page,Integer rows){
        // 使用分页助手开始分页,指定两个参数：当前页码，每页条数
        PageHelper.startPage(page, rows);
        // 然后分页拦截器会自动对接下来的查询进行分页
        List<TransitInfo> transitInfos = this.transitInfoMapper.select(null);// 不传查询条件

        for(TransitInfo t:transitInfos){
            // 放置了WayBill的信息
            t.setWayBill(wayBillMapper.selectByPrimaryKey(t.getWayBillId()));

            // 放置出入库信息 InoutStorageInfo
            List<InOutStorageInfo> inOutStorageInfoList = inoutStorageMapper.findInOutStorageInfoByTransitInfoId(t.getId());
            t.setInOutStorageInfos(inOutStorageInfoList);
            // 放置派送信息 DeliveryInfo------根据transitInfoId获取DevlieryInfo
            // TransitInfo的id和DeliveryInfo 的id一致
            DeliveryInfo deliveryInfo = deliveryInfoMapper.selectByPrimaryKey(t.getId());
            t.setDeliveryInfo(deliveryInfo);
            // 放置签收信息
            SignInfo signInfo = signMapper.selectByPrimaryKey(t.getId());
            t.setSignInfo(signInfo);

        }


        // 封装分页信息对象
        PageInfo<TransitInfo> pageInfo = new PageInfo<>(transitInfos);
        // 封装页面数据对象
        DataGridResult result = new DataGridResult(pageInfo.getTotal(), transitInfos);

        return result;

    }
















}
