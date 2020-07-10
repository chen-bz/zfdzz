package com.hzy.service.callback.impl;

import com.hzy.domain.PayOrder;
import com.hzy.service.base.BaseService;
import com.hzy.service.callback.CallbackPlatform;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RechargeCallbackPlatformImpl extends BaseService implements CallbackPlatform {


  @Override
  public String orderType() {
    return "R"; // 充值订单号前缀
  }

  /**
   * TODO 充值查询订单信息
   *
   * @param orderNo 订单号
   * @return
   */
  @Override
  public PayOrder queryOrderInfoResult(String orderNo) {
    log.info("==========支付查询订单============");
    PayOrder payOrder = new PayOrder();

    return payOrder;
  }

  /**
   * TODO 充值回调业务处理
   *
   * @param payOrder 订单
   */
  @Override
  public void callbackResult(PayOrder payOrder) {
    log.info("===========充值订单进入回调逻辑-开始=============订单号:" + payOrder.getOrderNo());
    // orderPayService.callBackRechargeOrder(payOrder);
    log.info("===========充值订单进入回调逻辑-结束=============订单号:" + payOrder.getOrderNo());
  }

  /*@Override
  public CircleOrder queryRefundOrderInfoResult(String orderNo) {
    return null;
  }*/

  @Override
  public void callbackRefundResult(Map<String, String> payOrder) {
  }
}
