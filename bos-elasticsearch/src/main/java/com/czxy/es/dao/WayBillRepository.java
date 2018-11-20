package com.czxy.es.dao;

import com.czxy.es.pojo.ESWayBill;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface WayBillRepository extends ElasticsearchRepository<ESWayBill,Integer> {
}
