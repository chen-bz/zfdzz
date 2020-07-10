package com.hzy.payment.wxpay;


import com.hzy.payment.wxpay.sdk.IWXPayDomain;
import com.hzy.payment.wxpay.sdk.WXPay;
import com.hzy.payment.wxpay.sdk.WXPayConfig;
import com.hzy.payment.wxpay.sdk.WXPayUtil;
import com.hzy.service.base.BaseService;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class MyConfig extends WXPayConfig {


	private byte[] certData;
	private BaseService baseService;

	private String key;

	private String type;
	private String setting;

	public MyConfig(BaseService baseService, String type) throws Exception {
		this.baseService = baseService;
		this.type = type;
		this.setting = "wxpay_";
		//不是沙箱环境要要下载证书，开出来
     /*String certPath = "/path/to/apiclient_cert.p12";
     File file = new File(certPath);
     InputStream certStream = new FileInputStream(file);
     this.certData = new byte[(int) file.length()];
     certStream.read(this.certData);
     certStream.close();*/
	}

	public MyConfig(BaseService baseService, String type, String setting) throws Exception {
		this.baseService = baseService;
		this.type = type;
		this.setting = setting;
		//不是沙箱环境要要下载证书，开出来
		Resource resource = new ClassPathResource("circles_apiclient_cert.p12");
		InputStream certStream = resource.getInputStream();
		this.certData = new byte[Long.valueOf(resource.contentLength()).intValue()];
		certStream.read(this.certData);
		certStream.close();
	}

	/** AppID */
	public String getAppID() {
		String appID = this.baseService.getSetting(setting + "setting", "appid");
		return appID;
	}

	/** 商户号 */
	public String getMchID() {
		String mchID = this.baseService.getSetting(setting + "setting",  "mch_id");
		return mchID;
	}

	/** 秘钥 */
	public String getKey() {
		String paykey = this.baseService.getSetting(setting + "setting",  "key");
		key = paykey;
		return paykey;
	}

	/** 证书流 */
	public InputStream getCertStream() {
		ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
		return certBis;
	}

	/** HTTP(S) 连接超时时间，单位毫秒 */
	public int getHttpConnectTimeoutMs() {
		return 8000;
	}

	/** HTTP(S) 读数据超时时间，单位毫秒 */
	public int getHttpReadTimeoutMs() {
		return 10000;
	}

	/**  */
	@Override
	public IWXPayDomain getWXPayDomain() {
// TODO Auto-generated method stub
		return WXPayDomainSimpleImpl.instance();
	}

	/**
	 * @param
	 * @return java.lang.String
	 * @作者 Chensb
	 * @描述 TODO 获取沙箱秘钥
	 * @日期 2018/12/8 18:14
	 */
	public String getSignKey() {
		try {
			String keys = this.baseService.getSetting(setting + "setting", "key");
			Map<String, String> data = new HashMap<String, String>();
			data.put("mch_id", this.getMchID());
			data.put("nonce_str", WXPayUtil.generateNonceStr());
			data.put("sign", WXPayUtil.generateSignature(data, keys));
			WXPay wxPay = new WXPay(this);
			String result = wxPay.requestWithoutCert("/sandboxnew/pay/getsignkey", data, 10000, 10000);
			Map<String, String> param = WXPayUtil.xmlToMap(result);
			return param.get("sandbox_signkey");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public void setBoxSignkey() {
		key = getSignKey();
	}
}