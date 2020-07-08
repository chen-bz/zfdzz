package com.hzy.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.hzy.service.base.BaseService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 阿里云短信接口
 *
 * @author zms
 */
@Component
@SuppressWarnings({"unchecked", "rawtypes"})
public class SendMessageUtil implements ApplicationContextAware {
  private static ApplicationContext applicationContext;
  // 产品名称:云通信短信API产品,开发者无需替换
  private static final String DYSMSAPI = "Dysmsapi";
  // 产品域名,开发者无需替换
  private static final String DOMAIN = "dysmsapi.aliyuncs.com";

  // 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
  private static String accessKeyId = ""; // 阿里大于提供的appkey
  private static String accessKeySecret = ""; // 阿里大于提供的secret
  private static String signName = "";

  /**
   * 发送短信
   *
   * @param mobile     手机号码
   * @param templateId 短信类型
   * @param smsContent 短信模板参数(json格式,如"{\"code\":\"1234\"}")
   * @return
   */
  public static Map sendSms(String mobile, String templateId, String smsContent) {
    try {
      // 可自助调整超时时间
      System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
      System.setProperty("sun.net.client.defaultReadTimeout", "10000");
      if (accessKeyId.equals("") || accessKeySecret.equals("") || signName.equals("")) {
        BaseService baseService = (BaseService) getBean("baseService");
        accessKeyId = baseService.getSetting("sms_setting", "access_key_id");
        accessKeySecret = baseService.getSetting("sms_setting", "access_key_secret");
        signName = baseService.getSetting("sms_setting", "sign_name");
      }
      // 初始化acsClient,暂不支持region化
      IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
      DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", DYSMSAPI, DOMAIN);
      IAcsClient acsClient = new DefaultAcsClient(profile);

      // 组装请求对象-具体描述见控制台-文档部分内容
      SendSmsRequest request = new SendSmsRequest();
      // 必填:待发送手机号
      request.setPhoneNumbers(mobile.trim());
      // 必填:短信签名-可在短信控制台中找到
      request.setSignName(signName);
      // 必填:短信模板-可在短信控制台中找到
      request.setTemplateCode(templateId);
      // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为"{\"name\":\"会员\",\"code\":\"123456\"}"
      request.setTemplateParam(smsContent);

      // 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
      // request.setSmsUpExtendCode("90997");

      // 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
      // request.setOutId("yourOutId");

      // hint 此处可能会抛出异常，注意catch

      SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
      return getResult(sendSmsResponse);
    } catch (Exception e) {
      e.printStackTrace();
      Map result = new HashMap();
      result.put("state", "0");
      result.put("msg", "短信发送失败，请先完善短信设置！");
      return result;
    }
  }

  /**
   * 解析发送短信返回结果
   *
   * @param sendSmsResponse 参数
   * @return
   */
  private static Map getResult(SendSmsResponse sendSmsResponse) {
    if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) { // 成功
      return ResultUtil.success("发送成功");
    } else {
      return ResultUtil.fail("抱歉，发送失败");
    }
  }

  /**
   * @param name 名称
   * @return Object
   * @throws BeansException
   * @方法名: getBean
   * @描述: TODO 获取bean
   * @返回类型: Object
   * @修改人：Chensb
   * @修改时间：Feb 11, 2018 9:34:11 AM @修改备注：
   */
  public static Object getBean(String name) throws BeansException {
    return applicationContext.getBean(name);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    SendMessageUtil.applicationContext = applicationContext;
  }

  // 测试
  public static void main(String[] args) {
    sendSms("13123396442", "2", "{\"name\":\"1234\"}");
  }

}
