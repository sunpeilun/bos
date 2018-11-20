package com.czxy.bos.domain.base;

import com.sun.xml.internal.bind.v2.model.core.ID;

import javax.persistence.*;

@Table(name = "t_courier")
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String checkPwd;// 查台密码
    private String company;//公司
    private String courierNum;// 工号
    private String deltag;// 是否删除  null--->未删除        0----->已删除    假删除
    private String name;//姓名
    private String pda;//查台账号
    private String telephone;//手机号码
    private String type;//快递员类型：小件员  中件员  大件员
    private String vehicleNum;//车牌号
    private String vehicleType;//车辆信息  卡车  小轿车  自行车  三轮车
    private Integer standardId;// 外键，取派标准表
    @Transient // 该属性不需要与数据库中的字段映射
    private Standard standard;

    private Integer taketimeId;//  外键  工作时间段  早班   中班  晚班  小夜班   大夜班
    @Transient
    private TakeTime taketime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCheckPwd() {
        return checkPwd;
    }

    public void setCheckPwd(String checkPwd) {
        this.checkPwd = checkPwd;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCourierNum() {
        return courierNum;
    }

    public void setCourierNum(String courierNum) {
        this.courierNum = courierNum;
    }

    public String getDeltag() {
        return deltag;
    }

    public void setDeltag(String deltag) {
        this.deltag = deltag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPda() {
        return pda;
    }

    public void setPda(String pda) {
        this.pda = pda;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVehicleNum() {
        return vehicleNum;
    }

    public void setVehicleNum(String vehicleNum) {
        this.vehicleNum = vehicleNum;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getStandardId() {
        return standardId;
    }

    public void setStandardId(Integer standardId) {
        this.standardId = standardId;
    }

    public Standard getStandard() {
        return standard;
    }

    public void setStandard(Standard standard) {
        this.standard = standard;
    }

    public Integer getTaketimeId() {
        return taketimeId;
    }

    public void setTaketimeId(Integer taketimeId) {
        this.taketimeId = taketimeId;
    }

    public TakeTime getTaketime() {
        return taketime;
    }

    public void setTaketime(TakeTime taketime) {
        this.taketime = taketime;
    }
}
