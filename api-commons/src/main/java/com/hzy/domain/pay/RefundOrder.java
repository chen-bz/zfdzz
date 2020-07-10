package com.hzy.domain.pay;

/**
 * @ClassName RefundOrder
 * @Description TODO
 * @Author Chensb
 * @Date 2019/10/23 11:06
 * @Version 1.0
 */
public class RefundOrder {

	/** 平台订单编号 */
	private String orderNo;
	/** 商户订单号单号 */
	private String tradeNo;
	/** 平台退款单号 */
	private String refundNo;
	/** 总金额 */
	private Integer totalFee;
	/** 退款金额 */
	private Integer refundFee;
	/** 退款原因 */
	private String refundDesc;

	/** 平台订单编号 */
	public String getOrderNo() {
		return orderNo;
	}

	/** 平台订单编号 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	/** 商户订单号单号 */
	public String getTradeNo() {
		return tradeNo;
	}

	/** 商户订单号单号 */
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	/** 平台退款单号 */
	public String getRefundNo() {
		return refundNo;
	}

	/** 平台退款单号 */
	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}

	/** 总金额 */
	public Integer getTotalFee() {
		return totalFee;
	}

	/** 总金额 */
	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}

	/** 退款金额 */
	public Integer getRefundFee() {
		return refundFee;
	}

	/** 退款金额 */
	public void setRefundFee(Integer refundFee) {
		this.refundFee = refundFee;
	}

	/** 退款原因 */
	public String getRefundDesc() {
		return refundDesc;
	}

	/** 退款原因 */
	public void setRefundDesc(String refundDesc) {
		this.refundDesc = refundDesc;
	}

}
