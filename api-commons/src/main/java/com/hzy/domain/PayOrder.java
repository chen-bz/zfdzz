package com.hzy.domain;

/**
 * 订单
 * @项目名称: third-party-payment
 * @类名: PayOrder
 * @描述: TODO
 * @作者: Administrator
 * @创建时间: 2018年6月29日 下午4:57:57 
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public class PayOrder {

	private Double needPayMoney; // 应付金额
	
	private String orderNo; // 订单号

	private String tradeNo; // 第三方订单号
	
	private String orderType; // 订单类型
	
	private Integer orderId; // 订单id
	
	private String sendTime; // 订单发送时间（请求支付时生成的时间）

	private String body; // 说明

	private String openid; // 微信openID

	private Integer memberId; // 下单会员

	private Integer circleId; // 圈子ID

	private String username; // 用户名

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getCircleId() {
		return circleId;
	}

	public void setCircleId(Integer circleId) {
		this.circleId = circleId;
	}

	public Double getNeedPayMoney() {
		return needPayMoney;
	}

	public void setNeedPayMoney(Double needPayMoney) {
		this.needPayMoney = needPayMoney;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}
}
