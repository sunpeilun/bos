package com.czxy;

import com.czxy.dao.ItemRepository;
import com.czxy.entity.Item;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.DoubleTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EsDemoApplication.class)//括号中的参数，表明启动测试，加载main函数，自动扫描资源
public class EsDemoApplicationTests {
    /**
     * 操作ElasticSearch的模板类
     */
    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private ItemRepository itemRepository;
    /**
     * 创建索引--类似创建数据库
     *
     */
    @Test
    public void testCreateIndex(){
        template.createIndex(Item.class);
    }

    /**
     * 让实体类映射到索引库中
     */
    @Test
    public void testPutMapping(){
        template.putMapping(Item.class);
    }


    /**
     * 删除索引库
     */
    @Test
    public void testDeleteIndex(){
        template.deleteIndex(Item.class);
    }

    /**
     * 保存数据
     * 当id一直的时候，会执行修改操作
     *
     *
     */
    @Test
    public void testSave(){
        List<Item> list = new ArrayList<>();


        Item i1 = new Item(2l, "小米手机9XS", "手机", "小米", 998.0, "http://www.baidu.com/123.jpg");
        Item i2 = new Item(3l, "华为9XSMax", "手机", "华为", 9998.0, "http://www.baidu.com/123.jpg");
        list.add(i1);
        list.add(i2);

        //保存
        itemRepository.saveAll(list);

    }

    @Test
    public void testQueryAll(){
        // 查找所有
        //Iterable<Item> list = this.itemRepository.findAll();
        // 对某字段排序查找所有 Sort.by("price").descending() 降序
        // Sort.by("price").ascending():升序
        Iterable<Item> list = this.itemRepository.findAll(Sort.by("price").ascending());

        for (Item item:list){
            System.out.println(item);
        }
    }


    @Test
    public void testFindByTitle(){
        List<Item> list = (List<Item>) this.itemRepository.findByTitle("华为");
        for (Item item:list){
            System.out.println(item);
        }

    }

    @Test
    public void testfindByPriceBetween(){
        List<Item> list = this.itemRepository.findByPriceBetween(100, 10000);
        for (Item item:list){
            System.out.println(item);
        }
    }
    @Test
    public void indexList() {
        List<Item> list = new ArrayList<>();
        list.add(new Item(9L, "小米手机7facebook", "手机", "小米", 3299.00, "http://image.baidu.com/13123.jpg"));
        list.add(new Item(10L, "坚果手机R1facebook", "手机", "锤子", 3699.00, "http://image.baidu.com/13123.jpg"));
        list.add(new Item(11L, "华为META10facebook", "手机", "华为", 4499.00, "http://image.baidu.com/13123.jpg"));
        list.add(new Item(12L, "小米Mix2Sfacebook", "手机", "小米", 4299.00, "http://image.baidu.com/13123.jpg"));
        list.add(new Item(13L, "荣耀V10facebook", "手机", "华为", 2799.00, "http://image.baidu.com/13123.jpg"));
        // 接收对象集合，实现批量新增
        itemRepository.saveAll(list);
    }

    /**
     *
     * termQuery
     * wildcardQuery
     * fuzzyquery
     * booleanQuery
     * numericRangeQuery
     *
     */
    @Test
    public void testMathQuery(){
        // 创建对象
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 在queryBuilder对象中自定义查询
        //matchQuery:底层就是使用的termQuery
        queryBuilder.withQuery(QueryBuilders.matchQuery("title","坚果"));
        //查询，search 默认就是分页查找
        Page<Item> page = this.itemRepository.search(queryBuilder.build());
        //获取数据
        long totalElements = page.getTotalElements();
        System.out.println("获取的总条数:"+totalElements);

        for(Item item:page){
            System.out.println(item);
        }


    }


    /**
     * termQuery:功能更强大，除了匹配字符串意外，还可以匹配int/long/double/float/....
     */
    @Test
    public void testTermQuery(){
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.termQuery("price",998.0));
        // 查找
        Page<Item> page = this.itemRepository.search(builder.build());

        for(Item item:page){
            System.out.println(item);
        }
    }

    @Test
    public void testBooleanQuery(){
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

        builder.withQuery(
                QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("title","华为"))
                                         .must(QueryBuilders.matchQuery("brand","华为"))
        );

        // 查找
        Page<Item> page = this.itemRepository.search(builder.build());
        for(Item item:page){
            System.out.println(item);
        }
    }


    @Test
    public void testFuzzyQuery(){
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.fuzzyQuery("title","faceoooo"));
        Page<Item> page = this.itemRepository.search(builder.build());
        for(Item item:page){
            System.out.println(item);
        }

    }


    @Test
    public void testPage(){
        int pageNo = 0;
        int pageSize = 2;

        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.matchQuery("category","手机"));
        // 分页
        // 第一个参数：第几页
        // 第二个参数：每页条数
        builder.withPageable(PageRequest.of(pageNo,pageSize));

        Page<Item> page = this.itemRepository.search(builder.build());

        for(Item item:page){
            System.out.println(item);
        }
    }




    @Test
    public void testOrder(){
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.matchQuery("category","手机"));

        builder.withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC));

        builder.withPageable(PageRequest.of(0,100));
        // 自动分页。默认查10条
        Page<Item> page = this.itemRepository.search(builder.build());

        for(Item item:page){
            System.out.println(item);
        }


    }


    /**
     * 以品牌分组，求组内情况
     */
    @Test
    public void testBucket(){
        //  自定义查询
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        // 聚合--分组
        // 此处我们采用哪种聚合类型？
        // 答：根据词条划分,对应的代码 AggregationBuilders.terms()
        //  terms("参数"):参数是返回值的键，最后通过键  获取值
        // field:列名：对哪个列聚合  AggregationBuilders.terms("brands").field("brand")
        // size: 结果条数  AggregationBuilders.terms("brands").field("brand").size(10)
        builder.addAggregation(AggregationBuilders.terms("brands").field("brand"));

        // 搜索
        // 此处的查询不是普通查询，是聚合查询，如果采用Page接收结果，那么很多值获取不到
        Page<Item> page = this.itemRepository.search(builder.build());
        // 所以需要将Page结果强制转成它的子类
        AggregatedPage<Item> aggregatedPage = (AggregatedPage<Item>) page;

        // page和aggregatedPage都是装的查询结果
        // 通过键 获取值
        Aggregation aggregation = aggregatedPage.getAggregation("brands");

        /**
         * 打印出结果
         **/
        StringTerms st = (StringTerms)aggregation;
        // 获取桶，桶内有结果
        List<StringTerms.Bucket> buckets = st.getBuckets();
        for(StringTerms.Bucket sb:buckets){
            System.out.println(sb.getKeyAsString()+":"+sb.getDocCount());
        }
    }


    /**
     * terms + avg
     *
     */
    @Test
    public void testMetric(){
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

        builder.addAggregation(AggregationBuilders.terms("brands").field("brand")
                                   .subAggregation(AggregationBuilders.avg("avgprice").field("price"))
        );

        // 查询
        AggregatedPage<Item> aggregatedPage = (AggregatedPage<Item>) this.itemRepository.search(builder.build());
        // 获取结果
        StringTerms stringTerms = (StringTerms) aggregatedPage.getAggregation("brands");

        // 获取桶
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        //遍历bucket
        for(StringTerms.Bucket sb:buckets){
            // 获取平均价格
             InternalAvg avg = (InternalAvg) sb.getAggregations().asMap().get("avgprice");

            System.out.println(sb.getKeyAsString()+":"+sb.getDocCount()+":"+avg.getValue());
        }

    }














}
