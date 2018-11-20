package com.czxy.bos.domain.transit;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @description: 签收信息
 */
@Entity
@Table(name = "T_SIGN_INFO")
public class SignInfo {

	/**
	 * 一个运输配送信息  对应 一个 签收信息，他们是一对一的关系
	 * 所以此处设计的时候 运输配送信息的id也是SignInfo 的id
	 */
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "SIGN_NAME")
	private String signName;

	@Column(name = "SIGN_TIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date signTime;

	@Column(name = "SIGN_TYPE")
	private String signType;

	@Column(name = "ERROR_REMARK")
	private String errorRemark;

	@Column(name = "DESCRIPTION")
	private String description; // 描述

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public Date getSignTime() {
		return signTime;
	}

	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getErrorRemark() {
		return errorRemark;
	}

	public void setErrorRemark(String errorRemark) {
		this.errorRemark = errorRemark;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
