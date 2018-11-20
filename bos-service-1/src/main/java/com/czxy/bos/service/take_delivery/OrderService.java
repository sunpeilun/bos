package com.czxy.bos.service.take_delivery;

import com.czxy.bos.dao.AreaMapper;
import com.czxy.bos.dao.CourierMapper;
import com.czxy.bos.dao.SubAreaMapper;
import com.czxy.bos.dao.take_delivery.OrderMapper;
import com.czxy.bos.dao.take_delivery.WorkBillMapper;
import com.czxy.bos.domain.base.Area;
import com.czxy.bos.domain.base.Courier;
import com.czxy.bos.domain.base.SubArea;
import com.czxy.bos.domain.take_delivery.Order;
import com.czxy.bos.domain.take_delivery.WorkBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AreaMapper areaMapper;

    @Autowired
    private SubAreaMapper subAreaMapper;

    public void saveOrder(Order order){
        /**
         * 解析Area地址
         */
        // 解析发货地址
        String sendAreaId = findAreaIdByAreaInfo(order.getSendArea());
        // 解析收货地址
        String recAreaId = findAreaIdByAreaInfo(order.getRecArea());
        // 将值设置给order
        order.setSendAreaId(sendAreaId);
        order.setRecAreaId(recAreaId);
        // 多设置一层
        order.getSendArea().setId(sendAreaId);
        order.getRecArea().setId(recAreaId);

        // 设置orderNum
        String orderNum = UUID.randomUUID().toString();
        order.setOrderNum(orderNum);
        // 设置orderTime
        order.setOrderTime(new Date());
        // 设置status
        order.setStatus("1");




        /**
         * 保存订单之后，有两种做法
         * 1将订单发送到MQ中，然后进行分单操作
         * 2直接在下面分单
         *
         * 采用第二种方法：
         * 方案：通过下单地址，找到分区，分区找到定区，再通过定区找到快递员
         * 难点：
         *     通过下单地址如何找到分区？
         *     根据详细地址：宿迁市沭阳县传智专修学院    ----->   找到对应的分区
         *
         *     思路：查出所有的分区信息，加载到内存中
         *         var index = sendAddress.indexOf(关键字|辅助关键字)
         *         index!=-1 --->找到了
         *
         *    第一步：先比较关键字---->当关键比较不出来的时候---->比较辅助关键字------>进入人工分单
         */
        //1 发件地址
        String sendAddress = order.getSendAddress();
        //2 发件区域
        Area sendArea = order.getSendArea();
        //3 获取所有的分区信息，将分区信息加载到内存中
        List<SubArea> subAreas = subAreaMapper.selectAll();
        //4 比较关键字
        List<SubArea> keyWordSubAreas = new ArrayList<>();
        //4.1 遍历subAreas
        for(SubArea subArea:subAreas){
            int index = sendAddress.indexOf(subArea.getKeyWords());
            if(index!=-1){
                // 添加到关键字集合中
                keyWordSubAreas.add(subArea);
            }
        }
        //4.2
        //    keyWordSubAreas.size==0------>比较辅助关键字
        //    keyWordSubAreas.size==1------>比较区域
        //    keyWordSubAreas.size>1------->比较辅助关键字
        //4.2.1  keyWordSubAreas.size==0------>比较辅助关键字
        if(keyWordSubAreas.size()==0){
            //4.2.1 比较辅助关键字
            List<SubArea> assistKeyWordSubArea = new ArrayList<>();
            // 遍历subAreas集合，比较辅助关键字
            for(SubArea subArea:subAreas){
                int index = sendAddress.indexOf(subArea.getAssistKeyWords());
                if(index!=-1){
                    assistKeyWordSubArea.add(subArea);
                }
            }
            //4.2.1.1
            // assistKeyWordSubArea.size==0  -->人工分单
            // assistKeyWordSubArea.size==1  -->比较区域
            // assistKeyWordSubArea.size>1?  -->比较区域
            //4.2.1.1 assistKeyWordSubArea.size==0
            if(assistKeyWordSubArea.size()==0){
                // 进入人工分单
                order.setOrderType("2");
            }else if(assistKeyWordSubArea.size()==1){
                //4.2.1.2 比较区域
                SubArea subArea = assistKeyWordSubArea.get(0);
                // 比较区域id
                if(subArea.getAreaId().equals(sendArea.getId())){
                    // 自动分单--已经确定了分区
                    Integer courierId = autoFD(subArea, order);
                    order.setCourierId(courierId);

                }else{
                    //人工分单
                    order.setOrderType("2");
                }
            }else if(assistKeyWordSubArea.size()>1){
                SubArea subArea = null;// 默认找不到匹配区域的area
                // 遍历assistKeyWordSubArea集合
                for(SubArea sa:assistKeyWordSubArea){
                    if(sa.getAreaId().equals(sendArea.getId())){
                        subArea=sa;
                        break;
                    }
                }
                // 判断subArea有没有赋值
                if(subArea==null){
                    // 人工分单
                    order.setOrderType("2");
                }else{
                    // 自动分单
                    Integer courierId = autoFD(subArea, order);
                    order.setCourierId(courierId);
                }
            }
        }else if(keyWordSubAreas.size()==1){
            // 4.2.2 比较区域
            // 比较区域是否一致
            SubArea subArea = keyWordSubAreas.get(0);
            if(subArea.getAreaId().equals(sendArea.getId())){
                // 自动分单
                Integer courierId = autoFD(subArea, order);
                order.setCourierId(courierId);
            }else{
                // 人工分单
                order.setOrderType("2");
            }

        }else if(keyWordSubAreas.size()>1){
            //4.2.3  比较辅助关键字 keyWordSubAreas.size >1
            List<SubArea> assistKeyWordSubArea = new ArrayList<>();
            // 只需要遍历关键字的集合，比较辅助关键字
            for(SubArea subArea:keyWordSubAreas){
                int index = sendAddress.indexOf(subArea.getAssistKeyWords());
                if(index!=-1){
                    assistKeyWordSubArea.add(subArea);
                }
            }
            // 符合 辅助关键字的分区信息已经到了assistKeyWordSubArea集合中
            // assistKeyWordSubArea.size == 0-->返回去比较区域
            // assistKeyWordSubArea.size == 1-->
            // assistKeyWordSubArea.size >  1--> 不考虑  辅助关键字和关键字一致
            if(assistKeyWordSubArea.size() == 0){
                // 关键字匹配了多个，而辅助关键字未匹配上
                // 比较区域
                List<SubArea> bjAreaIdList = new ArrayList<>();
                // 遍历keyWordSubAreas
                for(SubArea subArea:keyWordSubAreas){
                    // 比较当前的区域id和发件地址的areaid是否一直
                    if(subArea.getAreaId().equals(sendArea.getId())){
                        // 一致，添加到对比的集合中
                        bjAreaIdList.add(subArea);
                    }
                }
                // 判断bjAreaIdList集合的大小
                // bjAreaIdList.size ==0 --> 人工分单
                // bjAreaIdList.size ==1 --> 自动分单
                // bjAreaIdList.size >1  --> 人工分单
                if(bjAreaIdList.size()==1){
                    SubArea subArea = bjAreaIdList.get(0);
                    // 自动分单
                    Integer courierId = autoFD(subArea, order);
                    order.setCourierId(courierId);
                }else{
                    //人工分单
                    order.setOrderType("2");
                }


            }else if( assistKeyWordSubArea.size() == 1){
                // 比较区域
                SubArea subArea = assistKeyWordSubArea.get(0);
                if(subArea.getAreaId().equals(sendArea.getId())){
                    // 一致  自动分单
                    Integer courierId = autoFD(subArea, order);
                    order.setCourierId(courierId);
                }else{
                    // 人工分单
                    order.setOrderType("2");
                }

            }

        }


        // 分单成功之后，插入数据
        orderMapper.insert(order);

    }


    @Autowired
    private WorkBillMapper workBillMapper;

    @Autowired
    private CourierMapper courierMapper;
    /**
     * 何种情况？进入自动分单？-
     * 答：找出了唯一的分区-->自动分单
     *    流程：生成工单--向workbill表中插入一条数据
     *          发送短信--通知快递员
     * @param subArea
     */
    public Integer autoFD(SubArea subArea,Order order){
        //根据分区信息获取定区编号
        String fixedAreaId = subArea.getFixedAreaId();
        // 根据定区编号获取快递员
        List<Courier> list = courierMapper.findAssociationCouriers(fixedAreaId);
        // 定义快递员
        Courier courier = null;

        // 如果list.size == 0
        // 人工分工
        if(list.size()==0){
            //...
            return null;//结束当前方法的运行
        }else if(list.size()==1){
            // 自动分单
            courier = list.get(0);
        }else if(list.size()>1){
            // 比较时间.......
        }
        //已经找到快递员
        WorkBill workBill = new WorkBill();
        workBill.setType("新");
        workBill.setPickstate("新单");
        workBill.setBuildtime(new Date());
        workBill.setAttachbilltimes(0);// 追单次数
        workBill.setCourierId(courier.getId());
        workBill.setOrderNum(order.getOrderNum());
        // 保存
        workBillMapper.insert(workBill);
        // 短信通知
        System.out.println("短信通知到位");


        return courier.getId();
    }







    public String findAreaIdByAreaInfo(Area area){
        Example example = new Example(Area.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("province",area.getProvince());
        criteria.andEqualTo("city",area.getCity());
        criteria.andEqualTo("district",area.getDistrict());

        Area dbArea = areaMapper.selectOneByExample(example);

        if(dbArea!=null){
            return dbArea.getId();
        }
        return null;
    }



































}
