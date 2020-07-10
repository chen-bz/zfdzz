package com.hzy.payment.wxpay;

import com.hzy.payment.wxpay.sdk.WXPay;
import java.util.HashMap;
import java.util.Map;


public class WXPayExample {


  public static void main(String[] args) throws Exception {

    MyConfig config = new MyConfig(null, "");

    //WXPay pay = new WXPay(config);//正式

    WXPay wxpay = new WXPay(config, "http://yayihouse.com", false, true); //沙箱

    Map<String, String> data = new HashMap<String, String>();
    data.put("body", "腾讯充值中心-QQ会员充值"); //商品描述
    data.put("out_trade_no", "2016090910595900000012"); //商户订单号
    data.put("device_info", ""); //设备号，PC网页或公众号内支付可以传"WEB"
    data.put("fee_type", "CNY"); //标价币种，默认人民币：CNY
    data.put("total_fee", "1"); //标价金额，单位为分，
    data.put("spbill_create_ip", "123.12.12.123"); //APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
    data.put("notify_url", "http://www.example.com/pay/notify"); //异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
    data.put("trade_type", "NATIVE");  //交易类型   JSAPI：公众号支付，NATIVE：扫码支付，APP：APP支付 此处指定为扫码支付
    data.put("product_id", "12"); //trade_type=NATIVE时（即扫码支付），此参数必传。此参数为二维码中包含的商品ID，商户自行定义。
    try {
      Map<String, String> resp = wxpay.unifiedOrder(data);
      System.out.println(resp);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}