package com.czxy.bos.domain.transit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @description: 配送信息
 */
@Entity
@Table(name = "T_DELIVERY_INFO")
public class DeliveryInfo {


	/**
	 * 一个运输配送信息  生成  1  个派件信息  他们之间是一对一的关系
	 * 运输配送信息的  主键id   也是   派件 信息Delivery 的id
	 */
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "COURIER_NUM")
	private String courierNum;

	@Column(name = "COURIER_NAME")
	private String courierName;

	@Column(name = "DESCRIPTION")
	private String description; // 描述

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCourierNum() {
		return courierNum;
	}

	public void setCourierNum(String courierNum) {
		this.courierNum = courierNum;
	}

	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
