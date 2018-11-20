package com.czxy.es.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


/**
 * @description:运单实体类：包含了发件人的信息和收件人的信息
 * 在编写注解的时候，要关注搜索页面的几个信息
 *
 *   订单号
 *   发货地
 *   收货地
 *   快递产品类型编号：速运当日、速运次日、速运隔日
 *   运单状态： 1 待发货、 2 派送中、3 已签收、4 异常
 *
 *
 *
 */
@Document(indexName = "bos",type = "waybill",shards = 5,replicas = 1)
public class ESWayBill {
	@Id
	private Integer id;

	/**
	 * YD003
	 * y   d    003
	 *
	 */
	@Field(type = FieldType.Text)
	private String wayBillNum; // 运单编号
	@Field(type = FieldType.Keyword)
	private Integer orderId;// 订单号

	@Field(type = FieldType.Text,analyzer = "ik_max_word")
	private String sendName; // 寄件人姓名

	@Field(type = FieldType.Keyword)
	private String sendMobile;// 寄件人电话

	@Field(type = FieldType.Text,analyzer = "ik_max_word")
	private String sendCompany;// 寄件人公司

	@Field(type = FieldType.Keyword)
	private String sendAreaId;

	@Field(type = FieldType.Text,analyzer = "ik_max_word")
	private String sendAddress;// 寄件人详细地址信息

	@Field(type = FieldType.Text,analyzer = "ik_max_word")
	private String recName;// 收件人姓名

	@Field(type = FieldType.Keyword)
	private String recMobile;// 收件人电话

	@Field(type = FieldType.Text,analyzer = "ik_max_word")
	private String recCompany;// 收件人公司

	@Field(type = FieldType.Keyword)
	private String recAreaId;

	@Field(type = FieldType.Text,analyzer = "ik_max_word")
	private String recAddress;// 收件人详细地址信息

	@Field(type = FieldType.Keyword)
	private String sendProNum; // 快递产品类型编号：速运当日、速运次日、速运隔日
	@Field(type = FieldType.Keyword)
	private String goodsType;// 托寄物类型：文件、衣服 、食品、电子商品
	@Field(type = FieldType.Keyword)
	private String payTypeNum;// 支付类型编号：寄付日结、寄付月结、到付
	@Field(type = FieldType.Double)
	private Double weight;// 托寄物重量
	@Field(type = FieldType.Text,analyzer = "ik_max_word")
	private String remark; // 备注
	@Field(type = FieldType.Integer)
	private Integer num; // 原件数
	@Field(type = FieldType.Text,analyzer = "ik_max_word")
	private String arriveCity; // 到达地
	@Field(type = FieldType.Integer)
	private Integer feeitemnum; // 实际件数
	@Field(type = FieldType.Double)
	private Double actlweit; // 实际重量
	@Field(type = FieldType.Keyword)
	private String vol; // 体积 输入格式为1*1*1*1，表示长*宽*高*数量
	@Field(type = FieldType.Keyword)
	private String floadreqr; // 配载要求 (比如录入1=无，2=禁航，4=禁航空铁路)

	@Field(type = FieldType.Keyword)
	private String wayBillType; // 运单类型（类型包括：正常单据、异单、调单）
	/*
	 * 运单状态： 1 待发货、 2 派送中、3 已签收、4 异常
	 */
	@Field(type = FieldType.Integer)
	private Integer signStatus; // 签收状态

	/*
	 * 1、新增修改单据状态为“否” 2、作废时需将状态置为“是” 3、取消作废时需要将状态置为“否”
	 */
	@Field(type = FieldType.Keyword)
	private String delTag; // 作废标志

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getWayBillNum() {
		return wayBillNum;
	}

	public void setWayBillNum(String wayBillNum) {
		this.wayBillNum = wayBillNum;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getSendName() {
		return sendName;
	}

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}

	public String getSendMobile() {
		return sendMobile;
	}

	public void setSendMobile(String sendMobile) {
		this.sendMobile = sendMobile;
	}

	public String getSendCompany() {
		return sendCompany;
	}

	public void setSendCompany(String sendCompany) {
		this.sendCompany = sendCompany;
	}

	public String getSendAreaId() {
		return sendAreaId;
	}

	public void setSendAreaId(String sendAreaId) {
		this.sendAreaId = sendAreaId;
	}

	public String getSendAddress() {
		return sendAddress;
	}

	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}

	public String getRecName() {
		return recName;
	}

	public void setRecName(String recName) {
		this.recName = recName;
	}

	public String getRecMobile() {
		return recMobile;
	}

	public void setRecMobile(String recMobile) {
		this.recMobile = recMobile;
	}

	public String getRecCompany() {
		return recCompany;
	}

	public void setRecCompany(String recCompany) {
		this.recCompany = recCompany;
	}

	public String getRecAreaId() {
		return recAreaId;
	}

	public void setRecAreaId(String recAreaId) {
		this.recAreaId = recAreaId;
	}

	public String getRecAddress() {
		return recAddress;
	}

	public void setRecAddress(String recAddress) {
		this.recAddress = recAddress;
	}

	public String getSendProNum() {
		return sendProNum;
	}

	public void setSendProNum(String sendProNum) {
		this.sendProNum = sendProNum;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public String getPayTypeNum() {
		return payTypeNum;
	}

	public void setPayTypeNum(String payTypeNum) {
		this.payTypeNum = payTypeNum;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getArriveCity() {
		return arriveCity;
	}

	public void setArriveCity(String arriveCity) {
		this.arriveCity = arriveCity;
	}

	public Integer getFeeitemnum() {
		return feeitemnum;
	}

	public void setFeeitemnum(Integer feeitemnum) {
		this.feeitemnum = feeitemnum;
	}

	public Double getActlweit() {
		return actlweit;
	}

	public void setActlweit(Double actlweit) {
		this.actlweit = actlweit;
	}

	public String getVol() {
		return vol;
	}

	public void setVol(String vol) {
		this.vol = vol;
	}

	public String getFloadreqr() {
		return floadreqr;
	}

	public void setFloadreqr(String floadreqr) {
		this.floadreqr = floadreqr;
	}

	public String getWayBillType() {
		return wayBillType;
	}

	public void setWayBillType(String wayBillType) {
		this.wayBillType = wayBillType;
	}

	public Integer getSignStatus() {
		return signStatus;
	}

	public void setSignStatus(Integer signStatus) {
		this.signStatus = signStatus;
	}

	public String getDelTag() {
		return delTag;
	}

	public void setDelTag(String delTag) {
		this.delTag = delTag;
	}
}
