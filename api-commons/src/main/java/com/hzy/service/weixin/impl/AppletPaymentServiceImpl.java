package com.hzy.service.weixin.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.hzy.domain.PayLog;
import com.hzy.domain.PayOrder;
import com.hzy.domain.pay.OrderType;
import com.hzy.domain.pay.RefundOrder;
import com.hzy.domain.pay.SysMoneyRecord;
import com.hzy.domain.pay.SysMoneyType;
import com.hzy.mapper.PayLogMapper;
import com.hzy.mapper.PublicMapper;
import com.hzy.mapper.SysMoneyRecordMapper;
import com.hzy.mapper.SysMoneyTypeMapper;
import com.hzy.payment.wxpay.AESUtil;
import com.hzy.payment.wxpay.MyConfig;
import com.hzy.payment.wxpay.sdk.WXPay;
import com.hzy.payment.wxpay.sdk.WXPayConstants.SignType;
import com.hzy.payment.wxpay.sdk.WXPayUtil;
import com.hzy.service.base.BaseService;
import com.hzy.service.callback.CallbackPlatform;
import com.hzy.service.weixin.AppletPaymentService;
import com.hzy.util.DateUtil;
import com.hzy.util.PageData;
import com.hzy.util.PayUtil;
import com.hzy.util.ResultUtil;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @项目名称: third-party
 * @类名: PaymentServiceImpl
 * @描述: TODO
 * @作者: Chensb
 * @创建时间: 2018/12/6 17:26
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
@Slf4j
@Service
public class AppletPaymentServiceImpl extends BaseService implements AppletPaymentService {

  @Autowired
  private PublicMapper publicMapper;
  @Autowired
  private PayLogMapper payLogMapper;
  @Autowired
  private SysMoneyRecordMapper moneyRecordMapper;
  @Autowired
  private SysMoneyTypeMapper sysMoneyTypeMapper;

  //spring注入特殊功能 注入list
  ConcurrentHashMap<String, CallbackPlatform> callbackPlatformMap = new ConcurrentHashMap<>();

  @Autowired
  public AppletPaymentServiceImpl(List<CallbackPlatform> callbackPlatformList) {
    for (CallbackPlatform callbackPlatform : callbackPlatformList) {
      callbackPlatformMap.putIfAbsent(callbackPlatform.orderType(), callbackPlatform);
    }
  }

  /**
   * TODO 描述:统一下单
   *
   * @param orderNo
   * @param type
   * @return java.lang.Object
   * @author Chensb
   * @date 2019/12/10 15:15
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public Object appletAutograph(String orderNo, String type) {
    try {
      boolean isApp = false;
      if (type == null) {
        type = "activity_";
      }
      //String setting = "wxpay_";
      MyConfig config = new MyConfig(this, type);
      System.out.println("订单编号=" + orderNo);
      String testModel = this.getSetting("wxpay_setting", "test_model");
      PayOrder payOrder = null;
      //使用策略模式代替if else
      CallbackPlatform callbackPlatform = callbackPlatformMap.get(orderNo.substring(0, 1));
      if (callbackPlatform != null) {
        payOrder = callbackPlatform.queryOrderInfoResult(orderNo);// 获取订单信息
      } else {
        return ResultUtil.fail("请求出错-策略模式callbackPlatform is null");
      }
      //PayOrder payOrder = getOrder(orderNo); // 获取订单信息
      if ("1".equals(testModel)) { // 测试模式支付1分钱
        payOrder.setNeedPayMoney(0.01);
      }
      System.out.println("payOrder=" + JSONUtil.parseObj(payOrder).toString());
      Map<String, String> data = getMap(this, payOrder.getBody(), orderNo, payOrder.getNeedPayMoney(), payOrder.getOpenid(), 2); // 生成统一下单需要的参数
      data.put("attach", type); // 自定义参数  wxc8ee5af539bc06f2
      System.out.println("data=" + JSONUtil.parseObj(data).toString());
      WXPay wxpay = new WXPay(config); // 正式环境
      Map<String, String> result = wxpay.unifiedOrder(data);
      if (!WXPayUtil.isSignatureValid(result, config.getKey(), SignType.MD5)) {
        System.out.println("--==微信支付发起下单失败==--" + JSONUtil.parseObj(result).toString());
        return ResultUtil.fail("下单失败");
      } else {
        if (result.get("err_code_des") != null) {
          return ResultUtil.fail("下单失败," + result.get("err_code_des"));
        }
        System.out.println("++==微信支付发起下单成功==++");
        System.out.println(result.toString());
        String tradeType = data.get("trade_type");
        System.out.println("trade_type:" + tradeType);
        this.startPay(payOrder, result); // 记录日志
        data = new HashMap<String, String>();
        if ("APP".equals(tradeType)) { // APP调起支付参数
          String nonceStr = WXPayUtil.generateNonceStr();
          String timeStamp = DateUtil.getDateline() + "";
          HashMap<String, String> signData = new HashMap<String, String>();
          signData.put("prepayid", result.get("prepay_id"));
          signData.put("noncestr", nonceStr);
          signData.put("timestamp", timeStamp);
          signData.put("appid", config.getAppID());
          signData.put("partnerid", config.getMchID());
          signData.put("package", "Sign=WXPay");
          // 再次签名
          System.out.println("-----------type=" + type);
          System.out.println("+++++++++++key=" + config.getKey());
          String sign = WXPayUtil.generateSignature(signData, config.getKey(), SignType.MD5);
          data.put("prepayid", result.get("prepay_id"));
          data.put("noncestr", nonceStr);
          data.put("timestamp", timeStamp); // 时间戳
          data.put("appid", config.getAppID()); // appid
          data.put("partnerid", config.getMchID()); // 商户号
          data.put("package", "Sign=WXPay");
          data.put("sign", sign);
          System.out.println("-----==========调起支付参数=" + JSONUtil.parseObj(data).toString());
        } else if ("JSAPI".equals(tradeType)) { // JSAPI调起支付参数
          System.out.println("-----============JSAPI===============");
          data.put("appId", config.getAppID());
          data.put("timeStamp", DateUtil.getDateline() + "");
          data.put("nonceStr", WXPayUtil.generateNonceStr());
          data.put("package", "prepay_id=" + result.get("prepay_id"));
          data.put("signType", SignType.MD5.name());
          String sign = WXPayUtil.generateSignature(data, config.getKey());
          data.put("paySign", sign);
          data.remove("appId");
        } else if ("MWEB".equals(tradeType)) {
          data.put("mwebUrl", result.get("mweb_url"));
        }
        return ResultUtil.getResult("微信下单成功", data);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return ResultUtil.fail("请求出错");
    }
  }

  /**
   * TODO 描述:获取参数
   *
   * @param baseService
   * @param body
   * @param out_trade_no
   * @param total_fee
   * @param openid
   * @param isApp
   * @return java.util.Map<java.lang.String                                                                                                                               ,                                                                                                                               java.lang.String>
   * @author Chensb
   * @date 2019/12/10 15:15
   */
  public static Map<String, String> getMap(BaseService baseService, String body, String out_trade_no, Double total_fee, String openid, Integer isApp) {
    try {
      Map<String, String> map = new HashMap<String, String>();
      map.put("notify_url", baseService.getSetting("wxpay_setting", "pay_notify_url"));
      map.put("body", body);
      map.put("out_trade_no", out_trade_no);
      map.put("total_fee", NumberUtil.mul(total_fee, new BigDecimal(100.0)).intValue() + "");
      map.put("spbill_create_ip", baseService.getIp());
      if (isApp == 2) {
        map.put("openid", openid);
      }
      map.put("trade_type", "JSAPI");
      return map;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * TODO 描述:支付成功回调
   *
   * @param request
   * @param response
   * @return java.lang.Object
   * @author Chensb
   * @date 2019/12/10 15:15
   */
  @Override
  public Object onPayCallback(HttpServletRequest request, HttpServletResponse response) {
    Map<String, String> returnData = new HashMap<String, String>();
    try {
      String type = "activity_";
      System.out.println("微信支付回调");
      InputStream inStream = request.getInputStream();
      ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int len = 0;
      while ((len = inStream.read(buffer)) != -1) {
        outSteam.write(buffer, 0, len);
      }
      String resultXml = new String(outSteam.toByteArray(), "utf-8");
      Map<String, String> params = WXPayUtil.xmlToMap(resultXml);
      outSteam.close();
      inStream.close();
      String tradeType = params.get("trade_type");
      String attach = params.get("attach"); // 自定义参数,此处放的是小程序类别type字段,如:活动鸽是activity_  抽奖小程序是award_
      if (attach != null && !"".equals(attach)) {
        type = attach;
      }
      System.out.println("tradeType" + tradeType);
      //String setting = "applet_";
      MyConfig config = new MyConfig(this, type);
      if (!WXPayUtil.isSignatureValid(params, config.getKey())) {
        System.out.println("===============签名失败==============");
        // 支付失败
        returnData.put("return_code", "FAIL");
        returnData.put("return_msg", "return_code不正确");
        this.payCallback(params); // 记录日志
        return WXPayUtil.mapToXml(returnData);
      } else {
        System.out.println("===============付款成功==============");
        // ------------------------------
        // 处理业务开始
        // ------------------------------
        // 此处处理订单状态，结合自己的订单数据完成订单状态的更新
        // ------------------------------
        String tradeNo = params.get("transaction_id");
        // 修改支付日志的支付完成回调时间，返回参数写入相应字段里
        this.payCallback(params); // 记录日志
        // 再根据订单号查询一遍支付结果，确定支付完成再通知更改订单状态
        this.orderQuery(tradeNo, type);
        returnData.put("return_code", "SUCCESS");
        returnData.put("return_msg", "OK");
        return WXPayUtil.mapToXml(returnData);
      }
    } catch (Exception e) {
      try {
        returnData.put("return_code", "FAIL");
        returnData.put("return_msg", "服务器异常");
        return WXPayUtil.mapToXml(returnData);
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }
    return null;
  }

  /**
   * TODO 描述:支付成功回调再次查询支付信息
   *
   * @param transactionId
   * @param type
   * @return java.lang.Object
   * @author Chensb
   * @date 2019/12/10 15:16
   */
  @Override
  public Object orderQuery(String transactionId, String type) {
    try {
      System.out.println(type + "------微信支付查询------" + transactionId);
      Map<String, String> data = new HashMap<String, String>();
      data.put("transaction_id", transactionId);
      //String setting = "applet_";
      MyConfig config = new MyConfig(this, type);
      WXPay wxpay = new WXPay(config); // 正式
      Map<String, String> result = wxpay.orderQuery(data);
      System.out.println("------微信支付查询结果------" + JSONUtil.parseObj(result).toString());
      if (!WXPayUtil.isSignatureValid(result, config.getKey())) {
        // 支付失败-不处理业务，只记录查询结果
        System.out.println("---微信查询订单结果失败---" + JSONUtil.parseObj(result).toString());
        this.queryCallback(result); // 记录日志
      } else { // 支付成功，开始通知处理业务,--redis消息队列通知更改订单状态
        System.out.println("---微信查询订单结果成功---");
        this.queryCallback(result); // 记录日志
        //PayOrder payOrder = this.getOrder(result.get("out_trade_no"));
        PayOrder payOrder = new PayOrder();
        payOrder.setOrderNo(result.get("out_trade_no"));
        Integer totalFee = Integer.valueOf(result.get("total_fee"));
        Double money = NumberUtil.div(totalFee, new BigDecimal(100.0), 2).doubleValue();
        payOrder.setNeedPayMoney(money);
        payOrder.setTradeNo(result.get("transaction_id"));
        // this.setPayType(payOrder, "微信支付");
        //使用策略模式代替if else
        CallbackPlatform callbackPlatform = callbackPlatformMap.get(payOrder.getOrderNo().substring(0, 1));
        if (callbackPlatform != null) {
          callbackPlatform.callbackResult(payOrder);
        } else {
          System.out.println("---回调策略模式异常---");
        }
        // this.sendMessage(payOrder, 1); // 发送支付成功消息--告诉其他模块可以更改订单状态
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @param order 商户订单号
   * @return java.lang.Object
   * @作者 Chensb
   * @描述 TODO 申请退款
   * @日期 2018/12/7 14:45
   */
  @Override
  public Object refund(PayOrder order, String type) {
    try {
      if (type == null || "".equals(type)) {
        type = "activity_";
      }
      //CircleOrder order = null;
      //使用策略模式代替if else
   /*   CallbackPlatform callbackPlatform = callbackPlatformMap.get(circleOrder.getOrderNo().substring(0, 1));
      if (callbackPlatform != null) {
        order = callbackPlatform.queryRefundOrderInfoResult(circleOrder.getOrderNo());// 获取订单信息
      } else {
        return ResultUtil.fail("请求出错-策略模式callbackPlatform is null");
      }*/
      if (order == null) {
        return ResultUtil.fail("退款失败,订单不存在");
      }
      log.info("退款订单号:" + "order.getOrderNo()");
      log.info("微信商户交易号:" + "order.getTradeNo()");
      Map<String, String> data = new HashMap<String, String>();
      data.put("out_trade_no", "order.getOrderNo()"); // 商户订单号
      data.put("out_refund_no", "order.getTradeNo()"); // 微信商户交易号
      data.put("total_fee", NumberUtil.mul(order.getNeedPayMoney(), new BigDecimal(100)).intValue() + ""); // 订单总金额，单位为分，只能为整数 NumberUtil.mul(total_fee, 100).intValue() + ""
      data.put("refund_fee", NumberUtil.mul(new BigDecimal(0.01), new BigDecimal(100)).intValue() + ""); // 退款总金额，订单总金额，单位为分，只能为整数
      data.put("notify_url", this.getSetting("wxpay_setting", "refund_notify_url"));
      data.put("refund_desc", "接龙订单退款");
      String setting = "wxpay_";
      MyConfig config = new MyConfig(this, type, setting);
      WXPay wxpay = new WXPay(config); // 正式环境
      System.out.println("----------开始申请退款---------");
      Map<String, String> result = wxpay.refund(data);
      if (!WXPayUtil.isSignatureValid(result, config.getKey())) {
        System.out.println("===============退款失败==============");
        System.out.println("退款结果:" + JSONUtil.parseObj(result).toString());
        this.startRefund(order, result, type);
        // 退款失败
        return ResultUtil.fail("退款失败");
      } else {
        System.out.println("===============退款成功==============");
        // ------------------------------
        // 处理业务开始
        // ------------------------------
        // 此处处理订单状态，结合自己的订单数据完成订单状态的更新
        // ------------------------------
        String refundId = result.get("refund_id");
        // 记录日志
        this.startRefund(order, result, type);
        return ResultUtil.success("退款申请成功");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ResultUtil.fail("请求失败");
  }

  /**
   * @param request  请求
   * @param response 响应
   * @return java.lang.Object
   * @作者 Chensb
   * @描述 TODO 退款成功回调
   * @日期 2018/12/7 14:54
   */
  @Override
  public Object onRefundCallback(HttpServletRequest request, HttpServletResponse response) {
    Map<String, String> returnData = new HashMap<String, String>();
    try {
      String type = "activity_";
      System.out.println("微信退款回调");
      InputStream inStream = request.getInputStream();
      ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int len = 0;
      while ((len = inStream.read(buffer)) != -1) {
        outSteam.write(buffer, 0, len);
      }
      String resultXml = new String(outSteam.toByteArray(), "utf-8");
      Map<String, String> result = WXPayUtil.xmlToMap(resultXml);
      outSteam.close();
      inStream.close();
      //String setting = "applet_";
      MyConfig config = new MyConfig(this, type);
      String reqInfo = result.get("req_info");
      Map<String, String> res = null;
      if (reqInfo != null) {
        String reqInfos = AESUtil.decryptData(reqInfo, config.getKey()); // key是一样的所以直接使用活动鸽的就好
        System.out.println("解密结果:" + reqInfos);
        // <refund_recv_accout><![CDATA[_(:з」∠)_]]></refund_recv_accout>
        res = WXPayUtil.xmlToMap(reqInfos.replaceAll("<refund_recv_accout>\\S*</refund_recv_accout>", ""));
      }
      if ("SUCCESS".equals(result.get("return_code")) && "SUCCESS".equals(res.get("refund_status"))) {
        System.out.println("===============退款回调成功==============");
        // ------------------------------
        // 处理业务开始
        // ------------------------------
        // 此处处理订单状态，结合自己的订单数据完成订单状态的更新
        // ------------------------------
        String refundId = res.get("refund_id");
        // 修改支付日志的支付完成回调时间，返回参数写入相应字段里
        String tradeType = res.get("trade_type");
        // 记录日志
        this.refundCallback(res);
        // 再根据订单号查询一遍支付结果，确定支付完成再通知更改订单状态
        String outTradeNo = res.get("out_trade_no");
        type = this.getOrderType(outTradeNo);
        this.refundQuery(refundId, type);
        returnData.put("return_code", "SUCCESS");
        returnData.put("return_msg", "OK");
        return WXPayUtil.mapToXml(returnData);
      } else {
        // 退款失败
        System.out.println("===============退款回调失败==============");
        System.out.println("回调参数:" + JSONUtil.parseObj(res).toString());
        this.refundCallback(res);
        returnData.put("return_code", "FAIL");
        returnData.put("return_msg", "失败");
        return WXPayUtil.mapToXml(returnData);
      }
    } catch (Exception e) {
      e.printStackTrace();
      try {
        returnData.put("return_code", "FAIL");
        returnData.put("return_msg", "服务器异常");
        return WXPayUtil.mapToXml(returnData);
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }
    return null;
  }

  /**
   * @param refundId 微信退款单号
   * @return java.lang.Object
   * @作者 Chensb
   * @描述 TODO 查询退款--退款结果验证
   * @日期 2018/12/7 11:21
   */
  @Override
  public Object refundQuery(String refundId, String type) {
    try {
      System.out.println("==============退款回调成功,开始查询退款结果================");
      Map<String, String> data = new HashMap<String, String>();
      data.put("refund_id", refundId);
      //String setting = "applet_";
      MyConfig config = new MyConfig(this, type);
      WXPay wxpay = new WXPay(config); // 正式
      Map<String, String> result = wxpay.refundQuery(data);
      System.out.println("查询退款结果回调:" + JSONUtil.parseObj(result).toString());
      String orderNo = null;
      String refundSuccessTime = null;
      String refundFee = null;
      // String refund_success_time
      // refund_fee
      Set<String> keys = result.keySet();
      for (String key : keys) {
        if (key.indexOf("out_trade_no") == 0) {
          orderNo = result.get(key);
        }
        if (key.indexOf("refund_success_time") == 0) {
          refundSuccessTime = result.get(key);
        }
        if (key.indexOf("refund_fee") == 0) {
          refundFee = result.get(key);
        }
      }
      if (!WXPayUtil.isSignatureValid(result, config.getKey())) {
        // 退款失败-不处理业务，只记录查询结果
        System.out.println("--退款失败结果回调--");
        result.put("out_trade_no", orderNo);
        result.put("refund_success_time", refundSuccessTime);
        result.put("refund_fee", refundFee);
        // 记录日志
        this.queryRefundCallback(result);
      } else { // 退款成功，开始通知处理业务,--redis消息队列通知更改退款单状态
        // 记录日志
        result.put("out_trade_no", orderNo);
        result.put("refund_success_time", refundSuccessTime);
        result.put("refund_fee", refundFee);
        this.queryRefundCallback(result);
        //RefundOrder order = publicMapper.queryRefundOrder(new PageData("refundNo", orderNo, "type", type));
        //this.sendMessage(order); // 发送支付成功消息--告诉其他模块可以更改订单状态
        //使用策略模式代替if else
        System.out.println("回调成功退款金额:" + refundFee);
        CallbackPlatform callbackPlatform = callbackPlatformMap.get(orderNo.substring(0, 1));
        if (callbackPlatform != null) {
          callbackPlatform.callbackRefundResult(result);
        } else {
          System.out.println("---回调策略模式异常---");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @param orderNo 订单编号
   * @return com.hzy.domain.PayOrder
   * @作者 Chensb
   * @描述 TODO 查询订单
   * @日期 2018/12/6 20:43
   */
  @Override
  public PayOrder getOrder(String orderNo) {
    PayOrder payOrder = publicMapper.queryOrder(new PageData("orderNo", orderNo, "type", OrderType.getOrderType(orderNo)));
    return payOrder;
  }

  /**
   * TODO 描述:获取退款单信息
   *
   * @param refundNo 退款单号
   * @return com.hzy.domain.RefundOrder
   * @author Chensb
   * @date 2019/10/23 11:31
   */
  private RefundOrder getRefundOrder(String refundNo, String type) {
    RefundOrder order = publicMapper.queryRefundOrder(new PageData("refundNo", refundNo, "type", type));
    return order;
  }

  /**
   * @param payOrder 订单
   * @param payType  支付方式名称
   * @return
   * @作者 Chensb
   * @描述 TODO 设置支付方式
   * @日期 2018/12/30 18:25
   */
  @Override
  public void setPayType(PayOrder payOrder, String payType) {
    publicMapper.setPayType(new PageData("orderNo", payOrder.getOrderNo(), "type", payOrder.getOrderType(), "payType", payType, "payMoney", payOrder.getNeedPayMoney()));
  }

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
  @Override
  public Object transferAppletPay(String openId, String money, String rmoney, String type) {
    log.info("提现金额:" + money);
    //业务判断 openid是否有收款资格
    Map<String, Object> restmap = null;
    String APP_ID = this.getSetting("applet_setting", "circles_appid");
    String MCH_ID = this.getSetting("wxpay_setting", "mch_id");
    String API_SECRET = this.getSetting("wxpay_setting", "key");
    String TRANSFERS_PAY = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";//请求链接
    try {
      Map<String, Object> parm = new HashMap<String, Object>();
      parm.put("mch_appid", APP_ID); //小程序appid
      parm.put("mchid", MCH_ID); //商户号
      parm.put("nonce_str", RandomUtil.randomString(32)); //随机字符串
      parm.put("partner_trade_no", PayUtil.getRedPacketNo()); //商户订单号
      parm.put("openid", openId); //用户openid
      parm.put("check_name", "NO_CHECK"); //校验用户姓名选项 OPTION_CHECK
      //parm.put("re_user_name", "yy"); //check_name 设置为FORCE_CHECK或OPTION_CHECK，则必填
      parm.put("amount", money); //转账金额
      parm.put("desc", "领取提现"); //企业付款描述信息
      parm.put("spbill_create_ip", "193.112.150.171"); //Ip地址
      parm.put("sign", PayUtil.getSign(parm, API_SECRET));
      String restxml = HttpUtil.post(TRANSFERS_PAY, XmlUtil.mapToXmlStr(parm, null));
      restmap = XmlUtil.xmlToMap(restxml);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (MapUtil.isNotEmpty(restmap) && "SUCCESS".equals(restmap.get("result_code"))) {
      System.out.println("转账成功：" + restmap.get("err_code") + ":" + restmap.get("err_code_des"));
      Map<String, String> transferMap = new HashMap<>();
      transferMap.put("partner_trade_no", restmap.get("partner_trade_no").toString()); // 商户转账订单号
      transferMap.put("payment_no", restmap.get("payment_no").toString()); // 微信订单号
      transferMap.put("payment_time", restmap.get("payment_time").toString()); // 微信支付成功时间
      //财务记录
      /*
      SysMoneyRecord record = new SysMoneyRecord();
      record.setCreateTime(DateUtil.getDateline());
      record.setIsAdmin(0);
      record.setMemberId(0);
      record.setMoney(Double.parseDouble(rmoney));
      record.setOrderNo(restmap.get("payment_no"));
      record.setTradeNo(restmap.get("partner_trade_no"));
      record.setType(0);
      record.setTypeId(25);
      record.setRemark("用户【" + openId + "】" + "领取红包奖品支出");
      record.setTypeName("领取抽奖红包支出-微信企业付款到零钱");
      record.setRecordName("领取抽奖红包支出-微信企业付款到零钱");
      apiService.moneyRecordSave(record);
      */
      return ResultUtil.getResult("提现成功", transferMap);
    } else {
      if (MapUtil.isNotEmpty(restmap)) {
        System.out.println("转账失败：" + restmap.get("err_code") + ":" + restmap.get("err_code_des"));
      }
      return ResultUtil.fail(restmap.get("err_code").toString());
    }
  }


  /**
   * 开始支付记录日志
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public void startPay(PayOrder payOrder, Map<String, String> result) {
    PayLog log = new PayLog();
    log.setCreateTime(DateUtil.getDateline());
    log.setLogNo(log.createSn());
    log.setMoney(payOrder.getNeedPayMoney());
    log.setOrderNo(payOrder.getOrderNo());
    log.setOrderType(payOrder.getOrderType());
    log.setStatus(0);
    log.setPayType("微信支付");
    if (result != null) {
      log.setUnifiedResult(JSONUtil.parseObj(result).toString());
    }
    payLogMapper.insertSelective(log);
  }

  /**
   * 支付成功回调记录日志
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public void payCallback(Map<String, String> result) {
    PayLog log = payLogMapper.selectOneByExample(this.getAndExample(PayLog.class, "order_no=", result.get("out_trade_no")));
    log.setStatus(1);
    log.setCallbackTime(DateUtil.getDateline());
    log.setTradeNo(result.get("transaction_id"));
    if (result != null) {
      log.setCallbackResult(JSONUtil.parseObj(result).toString());
    }
    payLogMapper.updateByPrimaryKeySelective(log);
  }


  /**
   * 查询订单成功记录日志
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public void queryCallback(Map<String, String> result) {
    PayLog log = payLogMapper.selectOneByExample(this.getAndExample(PayLog.class, "order_no=", result.get("out_trade_no")));
    log.setStatus(2);
    log.setQueryResult(JSONUtil.parseObj(result).toString());
    log.setSuccessTime(DateUtil.getDateline(result.get("time_end"), "yyyyMMddHHmmss"));
    log.setPayMoney(NumberUtil.div(Integer.parseInt(result.get("cash_fee")), 100, 2)); // 现金支付金额
    payLogMapper.updateByPrimaryKeySelective(log);
  }


  /**
   * 开始退款记录日志
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public void startRefund(PayOrder order, Map<String, String> result, String type) {
    PayLog log = payLogMapper.selectOneByExample(this.getAndExample(PayLog.class, "order_no=", "Refund_" + order.getOrderNo()));
    if (log == null) {
      log = new PayLog();
    }
    log.setCreateTime(DateUtil.getDateline());
    log.setLogNo(log.createSn());
    log.setMoney(NumberUtil.div(0.0, 100, 2)); // 金额
    log.setOrderNo("Refund_" + order.getOrderNo());
    log.setOrderType("");
    log.setStatus(0);
    log.setPayType("微信退款");
    if (result != null) {
      log.setUnifiedResult(JSONUtil.parseObj(result).toString());
    }
    if (log.getLogId() == null) {
      payLogMapper.insertSelective(log);
    } else {
      payLogMapper.updateByPrimaryKey(log);
    }
  }

  /**
   * 退款成功回调记录日志
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public void refundCallback(Map<String, String> result) {
    PayLog log = payLogMapper.selectOneByExample(this.getAndExample(PayLog.class, "order_no=", "Refund_" + result.get("out_trade_no")));
    log.setStatus(1);
    log.setCallbackTime(DateUtil.getDateline());
    log.setTradeNo(result.get("transaction_id"));
    if (result != null) {
      log.setCallbackResult(JSONUtil.parseObj(result).toString());
    }
    payLogMapper.updateByPrimaryKeySelective(log);
  }


  /**
   * 查询退款成功记录日志
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public void queryRefundCallback(Map<String, String> result) {
    PayLog log = payLogMapper.selectOneByExample(this.getAndExample(PayLog.class, "order_no=", "Refund_" + result.get("out_trade_no")));
    log.setStatus(2);
    log.setQueryResult(JSONUtil.parseObj(result).toString());
    log.setSuccessTime(DateUtil.getDateline(result.get("refund_success_time"), "yyyyMMddHHmmss"));
    log.setPayMoney(NumberUtil.div(Integer.parseInt(result.get("refund_fee")), 100, 2)); // 现金支付金额
    payLogMapper.updateByPrimaryKeySelective(log);
  }

  /**
   * @param payOrder 订单信息
   * @param type     1:微信；2：支付宝；
   * @return
   * @作者 Chensb
   * @描述 TODO 保存资金记录
   * @日期 2019/1/6 21:41
   */
  @Async
  public void saveSysMoneyRecord(PayOrder payOrder, Integer type) {
    SysMoneyType moneyType = this.getMoneyTypeByOrder(payOrder, type);
    SysMoneyRecord record = new SysMoneyRecord();
    record.setCreateTime(DateUtil.getDateline());
    record.setIsAdmin(0);
    record.setMemberId(payOrder.getMemberId());
    record.setMoney(payOrder.getNeedPayMoney());
    record.setOrderNo(payOrder.getOrderNo());
    record.setTradeNo(payOrder.getTradeNo());
    record.setType(moneyType.getType());
    record.setTypeId(moneyType.getMoneyTypeId());
    record.setRemark("订单类别:" + payOrder.getOrderType() + "会员ID【" + payOrder.getMemberId() + "】" + moneyType.getRemark());
    record.setTypeName(moneyType.getTypeName());
    record.setRecordName(moneyType.getRemark());
    moneyRecordMapper.insertSelective(record);
  }

  /**
   * @param order     订单信息
   * @param orderType 1:微信；2：支付宝；
   * @return
   * @作者 Chensb
   * @描述 TODO 保存订单退款资金记录
   * @日期 2019/1/6 21:41
   */
  @Async
  public void saveSysMoneyRecord(RefundOrder order, String orderType) {
    SysMoneyRecord record = new SysMoneyRecord();
    record.setCreateTime(DateUtil.getDateline());
    record.setIsAdmin(0);
    record.setMoney(NumberUtil.div(order.getRefundFee(), new BigDecimal(100), 2).doubleValue());
    record.setOrderNo(order.getOrderNo());
    record.setTradeNo(order.getTradeNo());
    record.setType(0);
    record.setRemark("退款--订单类别:" + orderType);
    record.setTypeName("申请退款");
    record.setRecordName("申请退款");
    moneyRecordMapper.insertSelective(record);
  }


  /**
   * @param payOrder 订单信息
   * @param type     1:微信；2：支付宝；
   * @return com.hzy.domain.SysMoneyType
   * @作者 Chensb
   * @描述 TODO 查询金额类型
   * @日期 2019/1/6 21:43
   */
  private SysMoneyType getMoneyTypeByOrder(PayOrder payOrder, Integer type) {
    Integer moneyType = 0;
    String orderType = payOrder.getOrderType();
    if (orderType.equals(OrderType.ORDER.getTypeName())) {

    }
    return sysMoneyTypeMapper.selectByPrimaryKey(moneyType);
  }

  @Override
  public Object alipayRefundQuery(String tradeNo) {
    return null;
  }

  /**
   * TODO 描述:根据订单号前缀获取小程序类别,用于获取相应小程序配置信息
   *
   * @param tradeNo 订单号
   * @return java.lang.String
   * @author Chensb
   * @date 2019/12/10 15:14
   */
  private String getOrderType(String tradeNo) {
    String type = "activity_";
    String prefix = tradeNo.substring(0, 1);
    switch (prefix) {
      case "W": // 抽奖小程序
        type = "award_";
        break;
      default: // 默认活动鸽
        type = "activity_";
        break;
    }
    return type;
  }
}
