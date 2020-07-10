package com.hzy.service.weixin;

import com.hzy.domain.PayOrder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AppletPaymentService {
  /**
   * @作者 Chensb
   * @描述 TODO 统一下单
   * @日期 2018/12/6 19:34
   *
   * @param orderNo 订单编号
   * @return java.lang.Object
   */
  Object appletAutograph(String orderNo, String type);

  /**
   * @作者 Chensb
   * @描述 TODO 微信支付回调
   * @日期 2018/12/7 10:16
   *
   * @param request 请求
   * @param response 响应
   * @return java.lang.Object
   */
  Object onPayCallback(HttpServletRequest request, HttpServletResponse response);

  /**
   * @作者 Chensb
   * @描述 TODO 查询订单--支付结果验证
   * @日期 2018/12/7 11:21
   *
   * @param transactionId 微信订单号
   * @return java.lang.Object
   */
  Object orderQuery(String transactionId, String type);

  /**
   * @作者 Chensb
   * @描述 TODO 申请退款
   * @日期 2018/12/7 14:45
   *
   * @param circleOrder 微信订单号
   * @return java.lang.Object
   */
  Object refund(PayOrder circleOrder, String type);

  /**
   * @作者 Chensb
   * @描述 TODO 退款成功回调
   * @日期 2018/12/7 14:54
   *
   * @param request 请求
   * @param response 响应
   * @return java.lang.Object
   */
  Object onRefundCallback(HttpServletRequest request, HttpServletResponse response);

  /**
   * @作者 Chensb
   * @描述 TODO 查询退款--退款结果验证
   * @日期 2018/12/7 11:21
   *
   * @param refundId 微信退款单号
   * @return java.lang.Object
   */
  Object refundQuery(String refundId, String type);

  /**
   * @作者 Chensb
   * @描述 TODO 退款查询
   * @日期 2018/12/8 16:06
   *
   * @param tradeNo 支付宝订单号
   * @return java.lang.Object
   */
  Object alipayRefundQuery(String tradeNo);

  /**
   * @param orderNo 订单编号
   * @return com.hzy.domain.PayOrder
   * @作者 Chensb
   * @描述 TODO 查询订单
   * @日期 2018/12/6 20:43
   */
  PayOrder getOrder(String orderNo);

  /**
   * @作者 Chensb
   * @描述 TODO 设置支付方式
   * @日期 2018/12/30 18:25
   *
   * @param payOrder 订单
   * @param payType 支付方式名称
   * @return
   */
  void setPayType(PayOrder payOrder, String payType);

  /**
   * TODO 描述:企业微信付款(付款到个人钱包小程序)
   *
   * @param openId 小程序openID
   * @param money  转账金额(分)
   * @param rmoney 记录金额
   * @param type   小程序类别:抽奖小程序: award_
   * @return java.lang.Object
   * @author Chensb
   * @date 2019/12/17 10:00
   */
  Object transferAppletPay(String openId, String money, String rmoney, String type);
}
