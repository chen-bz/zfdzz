package com.hzy.service.weixin.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hzy.entity.MsgTemplate;
import com.hzy.service.base.BaseService;
import com.hzy.service.weixin.WeixinService;
import com.hzy.utils.PageData;
import com.hzy.utils.ResultUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName WeixinServiceImpl
 * @Description TODO 微信相关接口
 * @Author Chensb
 * @Date 2019/10/16 11:25
 * @Version 1.0
 */
@Service
public class WeixinServiceImpl extends BaseService implements WeixinService {
  private String APPID = "";
  private String SECRET = "";
  private static String SEND_SERVICE_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";

  /**
   * TODO 描述:获取接口调用凭证
   *
   * @return java.lang.String
   * @author Chensb
   * @date 2019/10/16 11:26
   */
  @Override
  public String getAccessToken() {
    this.getSetting();
    String accessToken = this.getRedis().get("xcx_access_token");
    System.out.println("缓存access_token:" + accessToken);
    if (isNull(accessToken)) {
      String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + APPID + "&secret=" + SECRET;
      String weixinJson = httpGet(url);
      JSONObject result = JSONUtil.parseObj(weixinJson);
      System.out.println("result:" + result.toString());
      if (result.get("access_token") != null) {
        accessToken = result.getStr("access_token");
        System.out.println("access_token:" + accessToken);
        this.getRedis().set("xcx_access_token", accessToken, 6900); // 微信凭证有效时间7200秒,缓存提前失效,重新获取
      } else {
        accessToken = "";
      }
    }
    return accessToken;
  }

  /**
   * TODO 描述:获取小程序码
   *
   * @param scene 小程序码值:
   *              最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~，
   *              其它字符请自行编码为合法字符（因不支持%，中文无法使用 urlencode 处理，请使用其他编码方式）
   * @param page  跳转页面:必须是已经发布的小程序存在的页面（否则报错），
   *              例如 pages/index/index, 根路径前不要填加 /,不能携带参数（参数请放在scene字段里），
   *              如果不填写这个字段，默认跳主页面
   * @return java.lang.String
   * @author Chensb
   * @date 2019/10/16 11:44
   */
  @Override
  public String getUnlimited(String scene, String page, String savePath, String fileName) {
    Map<String, Object> params = new HashMap<>();
    params.put("scene", scene);  //参数
    if (page != null) {
      params.put("page", page); //位置
    }
    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    HttpPost httpPost = new HttpPost("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + getAccessToken());  // 接口
    httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
    String body = JSONUtil.parseObj(params).toString(); // 必须是json模式的 post
    try {
      StringEntity entity = new StringEntity(body);
      entity.setContentType("image/png");
      httpPost.setEntity(entity);
      HttpResponse response = httpClient.execute(httpPost);
      InputStream inputStream = response.getEntity().getContent();
      String name = fileName + ".png";
      saveToImgByInputStream(inputStream, savePath, name);  //保存图片
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * TODO 描述:获取小程序码
   *
   * @param scene 小程序码值:
   *              最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~，
   *              其它字符请自行编码为合法字符（因不支持%，中文无法使用 urlencode 处理，请使用其他编码方式）
   * @param page  跳转页面:必须是已经发布的小程序存在的页面（否则报错），
   *              例如 pages/index/index, 根路径前不要填加 /,不能携带参数（参数请放在scene字段里），
   *              如果不填写这个字段，默认跳主页面
   * @return java.lang.String
   * @author Chensb
   * @date 2019/10/16 11:44
   */
  @Override
  public String getUnlimited(String scene, String page, HttpServletResponse response) {
    Map<String, Object> params = new HashMap<>();
    params.put("scene", scene);  //参数
    if (page != null) {
      params.put("page", page); //位置
    }
    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    HttpPost httpPost = new HttpPost("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + getAccessToken());  // 接口
    httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
    String body = JSONUtil.parseObj(params).toString(); // 必须是json模式的 post
    try {
      StringEntity entity = new StringEntity(body);
      entity.setContentType("image/png");
      httpPost.setEntity(entity);
      HttpResponse httpResponse = httpClient.execute(httpPost);
      httpResponse.getEntity().writeTo(response.getOutputStream());
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 将二进制转换成文件保存
   *
   * @param instreams 二进制流
   * @param imgPath   图片的保存路径
   * @param imgName   图片的名称
   * @return 1：保存正常
   * 0：保存失败
   */
  public static int saveToImgByInputStream(InputStream instreams, String imgPath, String imgName) {
    int stateInt = 1;
    if (instreams != null) {
      try {
        File file = new File(imgPath, imgName); // 可以是任何图片格式.jpg,.png等
        FileOutputStream fos = new FileOutputStream(file);
        byte[] b = new byte[1024];
        int nRead = 0;
        while ((nRead = instreams.read(b)) != -1) {
          fos.write(b, 0, nRead);
        }
        fos.flush();
        fos.close();
      } catch (Exception e) {
        stateInt = 0;
        e.printStackTrace();
      } finally {
      }
    }
    return stateInt;
  }

  /**
   * @param
   * @return
   * @作者 Chensb
   * @描述 TODO 获取配置信息
   * @日期 2018/12/12 21:09
   */
  private void getSetting() {
    APPID = this.getSetting("wxpay_setting", "appid");
    SECRET = this.getSetting("wxpay_setting", "secret");
  }

  /**
   * TODO 描述:发送客服消息
   *
   * @param openid
   * @return void
   * @author Chensb
   * @date 2019/12/3 9:50
   */
  @Override
  public void sendServiceMsg(String openid, String text, String url) {
    // eventPubLisher.pushMessageListener(text, openid, url);
  }

  /**
   * TODO 描述:发送小程序统一服务消息
   *
   * @param touser      openID
   * @param template_id 模板ID
   * @param page        小程序路径
   * @param data        小程序模板数据
   * @return void
   * @author Chensb
   * @date 2020/1/7 11:34
   */
  @Async
  @Override
  public void uniformSend(String touser, String template_id, String page, String data) {
    String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + getAccessToken();
    JSONObject param = new JSONObject();
    param.put("template_id", template_id);
    param.put("page", page);
    param.put("data", data);
    param.put("touser", touser);
    try {
      String res = this.httpPost(url, param.toString());
      System.out.println("服务消息发送结果:" + res);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * TODO 描述:检查是否关注公众号
   *
   * @param openid 公众号授权获取的openID
   * @return int
   * @author Chensb
   * @date 2020/4/8 15:45
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void updateUserIfSubscribe(String openid, HttpServletResponse response) {
    String accessToken = this.getWeixinAccessToken();
    if (!isNull(accessToken)) {
      String geiUserInfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + redis.get("kxq_access_token") + "&openid=" + openid + "&lang=zh_CN";
      JSONObject result = JSONUtil.parseObj(this.httpGet(geiUserInfoUrl));
      String unionid = result.get("unionid") != null ? result.getStr("unionid") : "";
      if (result.get("subscribe") != null && "1".equals(result.get("subscribe") + "")) {
        // memberMapper.updateSubscribe(new PageData("memberId", memberId));
        /*int upcount = memberMapper.updateSubscribe(new PageData("unionid", isNull(unionid) ? "0" : unionid, "subscribe", 1, "wxopenid", openid));
        if (!isNull(unionid) && upcount == 0) {
          this.redis.set("subscribe_" + openid, unionid);
          this.redis.set("subscribe_" + unionid, openid);
        }*/
        /*WxSubscribe subscribe = new WxSubscribe();
        subscribe.setWxUnionid(unionid);
        int count = wxSubscribeMapper.selectCount(subscribe);
        if (count == 0) {
          subscribe.setWxOpenid(openid);
          wxSubscribeMapper.insertSelective(subscribe);
        }*/
        // render(response, ResultUtil.getResult(new PageData("subscribe", 1)).toString());
      } else {
        // memberMapper.updateSubscribe(new PageData("unionid", isNull(unionid) ? "0" : unionid, "subscribe", 0, "wxopenid", openid));
        unionid = this.redis.get("subscribe_" + openid);
        this.redis.del("subscribe_" + openid);
        if (!isNull(unionid)) {
          this.redis.del("subscribe_" + unionid);
        }
      }
      // this.updateMember(openid);
    }
    // render(response, ResultUtil.getResult(new PageData("subscribe", 0)).toString());
  }

  /**
   * 发送内容。使用UTF-8编码。
   *
   * @param response response
   * @param text     text
   */
  public static void render(HttpServletResponse response, String text) {
    response.setContentType("application/json;charset=UTF-8");
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
    try {
      response.getWriter().write(text);
      response.getWriter().close();
    } catch (IOException e) {
    }
  }

  /**
   * TODO 描述:检查用户是否关注微信公众号
   *
   * @return java.util.Map
   * @author Chensb
   * @date 2020/4/13 13:46
   */
  @Override
  public Map checkMemberIfSubscribe(HttpServletResponse response) {
    // Member member = this.getLoginMember();
    String kxqAppid = this.getSetting("wxpay_setting", "kxq_appid");
    String kxqRedirectUri = null;
    try {
      kxqRedirectUri = URLEncoder.encode(this.getSetting("wxpay_setting", "kxq_redirect_uri"), "utf-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    String authorizeUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + kxqAppid + "&redirect_uri=" + kxqRedirectUri + "&response_type=code&scope=snsapi_base&state=" + "member.getOpenid()" + "#wechat_redirect";
    return ResultUtil.getResult(new PageData("url", authorizeUrl));
  }

  /**
   * TODO 描述:微信授权登录回调
   *
   * @param openid
   * @param code
   * @param state
   * @param response
   * @return void
   * @author Chensb
   * @date 2020/4/13 13:55
   */
  @Override
  public void wxLoginRedirect(String code, String state, HttpServletResponse response) {
    String kxqAppid = this.getSetting("wxpay_setting", "kxq_appid");
    String kxqSecret = this.getSetting("wxpay_setting", "kxq_secret");
    String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + kxqAppid + "&secret=" + kxqSecret + "&code=" + code + "&grant_type=authorization_code";
    String res = this.httpGet(url);
    JSONObject result = JSONUtil.parseObj(res);
    this.updateUserIfSubscribe(result.getStr("openid"), response);
  }

  @Override
  public String httpPost(String url, String param) throws IOException {
    CloseableHttpClient httpClient = null;
    BufferedReader bufferedReader = null;
    try {
      httpClient = HttpClientBuilder.create().build();
      HttpPost httpPost = new HttpPost(url);
      httpPost.setHeader("Content-Type", "application/json;charset=ut-8");
      if (StrUtil.isNotBlank(param)) {
        HttpEntity httpEntity = new StringEntity(param, "utf-8");
        httpPost.setEntity(httpEntity);
      }
      HttpResponse httpResponse = httpClient.execute(httpPost);
      if (httpResponse.getStatusLine().getStatusCode() != 200) {
        String errorLog = "请求失败，errorCode:" + httpResponse.getStatusLine().getStatusCode();
        //throw new HttpRequestException(url + errorLog);
      }
      //读取返回信息
      String output;
      bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "utf-8"));
      StringBuilder stringBuilder = new StringBuilder();
      while ((output = bufferedReader.readLine()) != null) {
        stringBuilder.append(output);
      }
      return stringBuilder.toString();
    } catch (IOException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (httpClient != null)
        httpClient.getConnectionManager().shutdown();
      if (bufferedReader != null)
        bufferedReader.close();
    }
  }

  @Async
  @Override
  public Object sendTemplateMsg(MsgTemplate template) {
    Map map = new HashMap(); // memberMapper.queryWxopenid(new PageData("memberId", template.getTouser()));
    if (map == null || "no".equals(map.get("wxopenid") + "")) {
      System.out.println("该用户无关注公众号,缺少wxopenid,会员ID:" + template.getTouser());
      return ResultUtil.fail("该用户无关注公众号,缺少wxopenid");
    }
    template.setTouser(map.get("wxopenid") + "");
    //this.getSetting();
    String accessToken = this.getWeixinAccessToken();
    String url = this.getSetting("wxpay_setting", "send_template_msg_url");
    url = url.replace("ACCESS_TOKEN", accessToken);
    template.setUrl(url);
    System.out.println("accessToken-url:" + url);
    // template.setUrl(url);
    // Setting setting = this.getSettingInfo("wx_msgtemp_setting", template.getTemplate_id());
    String datas = template.getData();
    String miniprograms = template.getMiniprogram();
    //template.setTemplate_id("s6euw4wbB27JwMB55Y_a7YaYqdMcK66jJNFA2GcLWo8");//下单通知
    JSONObject param = JSONUtil.parseObj(template);
    param.put("data", JSONUtil.parseObj(datas));
    param.put("miniprogram", JSONUtil.parseObj(miniprograms));
    // 暂时无链接
    param.remove("url");
    String weixinJson = "";
    try {
      weixinJson = this.httpPost(url, param.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("发送消息模板成功:" + JSONUtil.parseObj(weixinJson));
    return ResultUtil.getResult("发送成功", JSONUtil.parseObj(weixinJson));
  }

  /**
   * TODO 描述:获取公众号accessToken
   *
   * @param
   * @return java.lang.String
   * @author Chensb
   * @date 2020/4/23 14:46
   */
  @Override
  public String getWeixinAccessToken() {
    String accessToken = redis.get("kxq_access_token");
    if (isNull(accessToken)) {
      if (isTestModel()) {
        accessToken = httpGet("http://xft99.com/lsz/test_model_get_kxq_access_token");
      }
      String kxq_appid = this.getSetting("wxpay_setting", "kxq_appid");
      String kxq_secret = this.getSetting("wxpay_setting", "kxq_secret");
      String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + kxq_appid + "&secret=" + kxq_secret;
      JSONObject result = JSONUtil.parseObj(this.httpGet(url));
      accessToken = result.getStr("access_token");
      Long timeout = result.get("expires_in") != null ? result.getLong("expires_in") : 7000;
      redis.set("kxq_access_token", accessToken, timeout);
    }
    return accessToken;
  }

  /**
   * @param
   * @return java.lang.String
   * @作者 suyixiang
   * @描述 TODO 获取微信分享JsapiTicket
   * @日期 2018/12/19 17:55
   */
  @Override
  public String getWXJsapiTicket() {
    String ticket = this.redis.get("wx_jsapi_ticket");
    if (StrUtil.isBlank(ticket)) {
      this.getSetting();
      String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + getAccessToken() + "&type=jsapi";
      System.out.println("分享=========" + url);
      String resp = this.httpGet(url);
      JSONObject resJson = JSONUtil.parseObj(resp);
      System.out.println("获取到ticket：" + resJson.getStr("ticket"));
      this.getRedis().set("wx_jsapi_ticket", resJson.getStr("ticket"), 7000);
      return resJson.getStr("ticket");
    }
    return ticket;
  }

  @Override
  public String httpGet(String url) {
    HttpClient httpclient = HttpClientBuilder.create().build();
    HttpGet httpget = new HttpGet(url);
    HttpResponse response;
    try {
      response = httpclient.execute(httpget);
      HttpEntity entity = response.getEntity();
      String content = EntityUtils.toString(entity, "utf-8");

      return content;
    } catch (ClientProtocolException e) {
      e.printStackTrace();

    } catch (IOException e) {
      e.printStackTrace();

    }
    return null;
  }
}
