package com.czxy.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 一个简单的实体类如何与es结合呢？
 * indexName:文档的名字
 * type：文档的类型？答：随便写 doc  ppt  xls  story product
 * shards:elasticsearch默认集群，默认将一个数据放在6个位置
 * replicas:存放es的数据
 */
@Document(indexName = "item1",type = "docs",shards = 5,replicas = 1)
public class Item {
    @Id
    private Long id;// 主键
    // title的要求：分词，索引
    @Field(type = FieldType.Text,analyzer = "ik_max_word")//当类型是Text的时候，自动分词
    private String title;//商品标题
    @Field(type = FieldType.Keyword)//关键字会作为一个整体存在，不会被分词
    private String category;//分类
    @Field(type = FieldType.Keyword)
    private String brand;//品牌
    @Field(type = FieldType.Double)
    private Double price;//价格
    @Field(type = FieldType.Keyword,index = false)
    private String images;//图片


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Item() {
    }

    public Item(Long id, String title, String category, String brand, Double price, String images) {

        this.id = id;
        this.title = title;
        this.category = category;
        this.brand = brand;
        this.price = price;
        this.images = images;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                ", images='" + images + '\'' +
                '}';
    }
}
