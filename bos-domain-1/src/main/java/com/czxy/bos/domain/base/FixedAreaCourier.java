package com.czxy.bos.domain.base;

import javax.persistence.Table;

@Table(name = "t_fixedArea_courier")
public class FixedAreaCourier {

    private String fixedAreaId;
    private Integer courierId;

    public String getFixedAreaId() {
        return fixedAreaId;
    }

    public void setFixedAreaId(String fixedAreaId) {
        this.fixedAreaId = fixedAreaId;
    }

    public Integer getCourierId() {
        return courierId;
    }

    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }
}
