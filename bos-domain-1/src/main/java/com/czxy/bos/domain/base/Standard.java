package com.czxy.bos.domain.base;

import com.sun.xml.internal.bind.v2.model.core.ID;

import javax.persistence.*;
import java.util.Date;

@Entity//标明这是一个实体，可不添加
@Table(name = "t_standard") // 对应表名
public class Standard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mysql的自增长策略
    private Integer id;
    @Column(name = "MAX_LENGTH")
    private Integer maxLength;//最大长度
    @Column(name = "MAX_WEIGHT")
    private Integer maxWeight;//最大重量
    @Column(name = "MIN_LENGTH")
    private Integer minLength;// 最小长度
    @Column(name = "MIN_WEIGHT")
    private Integer minWeight;//最小重量
    @Column(name = "NAME")
    private String name;// 取派标准 的名字
    @Column(name = "OPERATING_COMPANY")
    private String operatingCompany;// 操作公司
    @Column(name = "OPERATING_TIME")
    private Date operatingTime;//操作时间
    @Column(name = "OPERATOR")
    private String operator;//操作员

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(Integer maxWeight) {
        this.maxWeight = maxWeight;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(Integer minWeight) {
        this.minWeight = minWeight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperatingCompany() {
        return operatingCompany;
    }

    public void setOperatingCompany(String operatingCompany) {
        this.operatingCompany = operatingCompany;
    }

    public Date getOperatingTime() {
        return operatingTime;
    }

    public void setOperatingTime(Date operatingTime) {
        this.operatingTime = operatingTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
