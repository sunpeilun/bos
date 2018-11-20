package com.czxy.dao;

import com.czxy.entity.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 参数：一个是操作的对象，一个的主键的类型
 *
 */
public interface ItemRepository extends ElasticsearchRepository<Item,Long> {
    /**
     * 自定义方法查询，只需要定义方法，就可以自动查询
     *
     * @param title
     * @return
     */
    public List<Item> findByTitle(String title);

    public List<Item> findByPriceBetween(double p1,double p2);

}
