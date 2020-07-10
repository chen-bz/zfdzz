package com.hzy.domain;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hzy.service.base.BaseService;
import java.util.Iterator;

/**
 * @项目名称: third-party
 * @类名: MsgTemplate
 * @描述: TODO
 * @作者: Chensb
 * @创建时间: 2018/12/13 17:27
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public class MsgTemplate {

  public MsgTemplate(){};

  /**
   * @作者 Chensb
   * @描述 TODO
   * @日期 2019/1/15 12:45
   *
   * @param touser 给谁发
   * @param templateId 模板ID
   * @param type 类别
   * @param data 数据json字符串
   * @return
   */
  public MsgTemplate(String touser, String templateId, String type, String data) {
    this.touser = touser;
    this.template_id = templateId;
    this.url = type;
    if (!"".equals(data)) {
      this.data = data;
    }
  }

  /**
   * @作者 Chensb
   * @描述 TODO
   * @日期 2019/1/15 12:45
   *
   * @param touser 给谁发
   * @param templateId 模板ID
   * @param type 类别
   * @param data 数据json
   * @return
   */
  public MsgTemplate(String touser, String templateId, String type, JSONObject data) {
    this.touser = touser;
    this.template_id = templateId;
    this.url = type;
    this.data = data.toString();
  }

  /**
   * @作者 Chensb
   * @描述 TODO 封装数据
   * @日期 2019/1/15 12:45
   *
   * @param baseService
   * @param settingKey 设置key
   * @param datas
   * @return net.sf.json.JSONObject
   */
  public static JSONObject getData(BaseService baseService, String settingKey, String... datas) {
    Setting setting = baseService.getSettingInfo("wx_msgtemp_setting", settingKey);
    JSONObject dataJson = JSONUtil.parseObj(setting.getType_json());
    Iterator<String> keys = dataJson.keySet().iterator();
    for (String value : datas) {
      if (keys.hasNext()) {
        String key = keys.next();
        JSONObject item = dataJson.getJSONObject(key);
        item.put("value", value);
        dataJson.put(key, item);
      }
    }
    return dataJson;
  }


  /**
   * 用户openID
   */
  private String touser;
  /**
   * 模板消息ID（后台配置key）
   */
  private String template_id;
  /**
   * 点击跳转链接：接受消息时当类别判断使用
   */
  private String url;

  /**
   * 数据json字符串
   */
  private String data;

  /**
   * 数跳小程序所需数据
   */
  private String miniprogram;


  public String getMiniprogram() {
    return miniprogram;
  }

  public void setMiniprogram(String miniprogram) {
    this.miniprogram = miniprogram;
  }

  /**
   * 用户openID
   */
  public String getTouser() {
    return touser;
  }

  /**
   * 用户openID
   */
  public void setTouser(String touser) {
    this.touser = touser;
  }

  /**
   * 模板消息ID（后台配置key）
   */
  public String getTemplate_id() {
    return template_id;
  }

  /**
   * 模板消息ID（后台配置key）
   */
  public void setTemplate_id(String template_id) {
    this.template_id = template_id;
  }

  /**
   * 点击跳转链接
   */
  public String getUrl() {
    return url;
  }

  /**
   * 点击跳转链接
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * 数据json字符串
   */
  public String getData() {
    return data;
  }

  /**
   * 数据json字符串
   */
  public void setData(String data) {
    this.data = data;
  }
}
