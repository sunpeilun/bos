package com.czxy.bos.domain.transit;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.czxy.bos.domain.take_delivery.WayBill;
import org.apache.commons.lang3.StringUtils;

/**
 * @description: 运输配送信息
 */
@Entity
@Table(name = "T_TRANSIT_INFO")
public class TransitInfo {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	/**
	 * 一个运单生成一个运输配送信息
	 */
	@Transient
	private WayBill wayBill;
	@Column(name = "WAYBILL_ID")
	private Integer wayBillId;

	/**
	 * 一个运输配送信息生成多个出入库信息  当入库的信息和 outletaddress一致的时候，就需要开始派件
	 *
	 */
	@Transient
	private List<InOutStorageInfo> inOutStorageInfos = new ArrayList<InOutStorageInfo>();

	/**
	 * 一个运输配送信息  生成  一个  派件信息
	 *
	 */
	@Transient
	private DeliveryInfo deliveryInfo;
	@Column(name = "DELIVERY_INFO_ID")
	private Integer deliveryInfoId;


	/**
	 * 一个运输配送信息  生成  一个  签收信息
	 */
	@Transient
	private SignInfo signInfo;
	@Column(name = "SIGN_INFO_ID")
	private Integer signInfoId;
	@Column(name = "STATUS")
	// 出入库中转、到达网点、开始配置/派件、正常签收、异常
	private String status;

	// 目的地
	@Column(name = "OUTLET_ADDRESS")
	private String outletAddress;


	// 拼接物流信息
	@Transient// 不想它与数据库中的字段映射
	private String transitInfo;

	public String getTransitInfo() {
		String info = "";
		// 拼接出入库信息
		for(InOutStorageInfo inOutStorageInfo:inOutStorageInfos){
			info+="【"+inOutStorageInfo.getOperation()+"】"+inOutStorageInfo.getDescription()+"<br/>";
		}
		// 拼接派送信息
		if(deliveryInfo!=null){
			info+="【开始派送】"+deliveryInfo.getDescription()+"<br/>";
		}
		// 拼接签收信息
		if(signInfo!=null){
			info+="【已签收】"+signInfo.getDescription();
		}

		// 如果info还是为空呢？
		if(StringUtils.isBlank(info)){
			info="【暂无信息】";
		}


		return info;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public WayBill getWayBill() {
		return wayBill;
	}

	public void setWayBill(WayBill wayBill) {
		this.wayBill = wayBill;
	}

	public List<InOutStorageInfo> getInOutStorageInfos() {
		return inOutStorageInfos;
	}

	public void setInOutStorageInfos(List<InOutStorageInfo> inOutStorageInfos) {
		this.inOutStorageInfos = inOutStorageInfos;
	}

	public DeliveryInfo getDeliveryInfo() {
		return deliveryInfo;
	}

	public void setDeliveryInfo(DeliveryInfo deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
	}

	public SignInfo getSignInfo() {
		return signInfo;
	}

	public void setSignInfo(SignInfo signInfo) {
		this.signInfo = signInfo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOutletAddress() {
		return outletAddress;
	}

	public void setOutletAddress(String outletAddress) {
		this.outletAddress = outletAddress;
	}
	public Integer getWayBillId() {
		return wayBillId;
	}

	public void setWayBillId(Integer wayBillId) {
		this.wayBillId = wayBillId;
	}

	public Integer getDeliveryInfoId() {
		return deliveryInfoId;
	}

	public void setDeliveryInfoId(Integer deliveryInfoId) {
		this.deliveryInfoId = deliveryInfoId;
	}

	public Integer getSignInfoId() {
		return signInfoId;
	}

	public void setSignInfoId(Integer signInfoId) {
		this.signInfoId = signInfoId;
	}
}
