package com.czxy.bos.service.take_delivery;

import com.czxy.bos.dao.take_delivery.WayBillMapper;
import com.czxy.bos.domain.take_delivery.WayBill;
import com.czxy.bos.domain.vo.DataGridResult;
import com.czxy.es.dao.WayBillRepository;
import com.czxy.es.pojo.ESWayBill;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class WayBillService  {

    @Autowired
    private WayBillMapper wayBillMapper;


    public void saveWayBill(WayBill wayBill){
        wayBillMapper.insert(wayBill);

        // 将数据同步保存到elasticsearch中:此方法在运单快速录入、录单录入、运单导入中都需要添加
        ESWayBill esWayBill = new ESWayBill();
        BeanUtils.copyProperties(wayBill,esWayBill);

        wayBillRepository.save(esWayBill);
    }


    @Autowired
    private WayBillRepository wayBillRepository;

    /***
     * 根据页面的请求，查找elasticsearch，并返回查找到的数据
     *
     * TermQuery、matchQuery、wildcardQuery、fuzzyQuery、booleanQuery
     *
     *  根据这5个条件差？用哪种查询？答：使用组合查询
     *
     *
     *
     *      1 订单号   y      d     003     0    3     00   3--->选择何种查询？答：wildcardQuery  *?*
     *  *   2 发货地  北京市顺义区   北  京  市  北京  顺义 义区--->选择何种查询？答：wildcardQuery  *?*
     *  *   3 收货地
     *  *   4 快递产品类型编号：速运当日、速运次日、速运隔日------->termQuery/matchQuery
     *  *   5 运单状态： 1 待发货、 2 派送中、3 已签收、4 异常---->
     *
     */
    public DataGridResult conditionQuery(WayBill wayBill,Integer page,Integer rows){
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        // 0 先创建1个booleanQuery
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();



        // 1 判断订单号
        if(StringUtils.isNotBlank(wayBill.getWayBillNum())){

            WildcardQueryBuilder wayBillNumQueryBuilder = QueryBuilders.wildcardQuery("wayBillNum", "*" + wayBill.getWayBillNum().toLowerCase() + "*");
            // 组合到boolQuery
            boolQuery.must(wayBillNumQueryBuilder);
        }
        // 2 发货地
        if(StringUtils.isNotBlank(wayBill.getSendAddress())){
            // 第一次的代码，发现了问题：输入啥都可以，就是不能输入 北京顺义
//            WildcardQueryBuilder sendAddressQueryBuilder = QueryBuilders.wildcardQuery("sendAddress", "*" + wayBill.getSendAddress() + "*");
//            boolQuery.must(sendAddressQueryBuilder);
            // 1 改进
            // 2 改进的第一个问题：北京市顺义区   北  京  市  北京  顺义 义区   这种方式允许查吗？
            WildcardQueryBuilder sendAddressQueryBuilder = QueryBuilders.wildcardQuery("sendAddress", "*" + wayBill.getSendAddress() + "*");
            // 3 改进的第二个问题：北京顺义    这种方式要被允许-->这种方式需要对 查询词条进行分词后   再进行查询
            // QueryStringQueryBuilder此对象会对查询的词条分词后的各种情况进行分词查找
            // 参数：就是前台传过来的查询内容
//            QueryStringQueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(wayBill.getSendAddress());
//            queryStringQueryBuilder.field("sendAddress").defaultOperator(Operator.AND);
            QueryStringQueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(wayBill.getSendAddress()).field("sendAddress").defaultOperator(Operator.AND);

            //4  sendAddressQueryBuilder 和  queryStringQueryBuilder  这两个查询的关系是什么？---should --should
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().should(sendAddressQueryBuilder).should(queryStringQueryBuilder);

            // 5 加进最外面的booleanQuery
            boolQuery.must(boolQueryBuilder);

        }
        //3 收货
        if(StringUtils.isNotBlank(wayBill.getRecAddress())){
            WildcardQueryBuilder recAddressQueryBuilder = QueryBuilders.wildcardQuery("recAddress", "*" + wayBill.getRecAddress() + "*");
            boolQuery.must(recAddressQueryBuilder);
        }
        //4 速运当日、速运次日、速运隔日
        if(StringUtils.isNotBlank(wayBill.getSendProNum())){
            TermQueryBuilder sendProNumQueryBuilder = QueryBuilders.termQuery("sendProNum", wayBill.getSendProNum());
            boolQuery.must(sendProNumQueryBuilder);
        }
        //5 运单状态： 1 待发货、 2 派送中、3 已签收、4 异常
        if(wayBill.getSignStatus()!=null && wayBill.getSignStatus()!=0){
            TermQueryBuilder signStatusQueryBuilder = QueryBuilders.termQuery("signStatus", wayBill.getSignStatus());
            boolQuery.must(signStatusQueryBuilder);
        }

        // 执行分页
        builder.withPageable(PageRequest.of(page-1,rows));

        // 执行查询
        builder.withQuery(boolQuery);
        Page<ESWayBill> list = this.wayBillRepository.search(builder.build());

        //拼接返回结果
        DataGridResult result = new DataGridResult();
        result.setTotal(list.getTotalElements());

        result.setRows(list.getContent());

        return result;
    }

    /**
     * 同步索引
     */
    public void sysncIndex(){
        // 只需要更新当天修改过的数据
        List<WayBill> list = wayBillMapper.selectAll();

        List<ESWayBill> eslist = new ArrayList<>();
        for(WayBill wb:list){
            ESWayBill eswb = new ESWayBill();
            BeanUtils.copyProperties(wb,eswb);
            eslist.add(eswb);
        }

        // 先删除所有的数据
        this.wayBillRepository.deleteAll();

        // 调用Respository
        this.wayBillRepository.saveAll(eslist);
    }


    public List<WayBill> findAll(){
        return wayBillMapper.selectAll();
    }


}
