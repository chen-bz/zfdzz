package com.hzy.service.callback;

import com.hzy.domain.PayOrder;
import java.util.Map;

/**
 * @author syx
 * 订单支付回调实现接口
 */
public interface CallbackPlatform {

  /**
   * @return
   * @作者 syx
   * @描述 TODO 订单类型(订单号前缀)
   * @日期 2020年03月03日 上午11:37:45
   */
   String  orderType();

  /**
   * TODO 支付查询订单信息
   * @param orderNo 订单号
   */
   PayOrder queryOrderInfoResult(String orderNo);

  /**
   * TODO 支付回调业务处理
   * @param payOrder 订单
   */
   void callbackResult(PayOrder payOrder);

  /**
   * TODO 退款查询订单信息
   * @param orderNo 订单号
   */
   // CircleOrder queryRefundOrderInfoResult(String orderNo);

  /**
   * TODO 退款回调业务处理
   * @param payOrder 订单
   */
   void callbackRefundResult(Map<String, String> payOrder);

}
