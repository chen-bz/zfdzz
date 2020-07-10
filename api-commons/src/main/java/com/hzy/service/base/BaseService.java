package com.hzy.service.base;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hzy.domain.Setting;
import com.hzy.mapper.SettingsMapper;
import com.hzy.utils.MyExample;
import com.hzy.utils.PageData;
import com.hzy.utils.RedisOperator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * .
 *
 * @项目名称: xtx-api
 * @类名: BaseService
 * @描述: TODO
 * @作者: Chensb
 * @创建时间: 2018年8月23日 上午9:28:31
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
@Component
public class BaseService {

  private static final long serialVersionUID = 6357869213649815545L;

  @Resource
  protected RedisOperator redis;
  @Resource
  protected SettingsMapper settingsMapper;

  /**
   * .
   * 得到PageData
   */
  public PageData getPageData() {
    PageData pd = new PageData(this.getRequest());
    Object[] keys = pd.keySet().toArray();
    for (Object key : keys) {
      if ("".equals(pd.get(key.toString()))) {
        pd.remove(key.toString());
      }
    }
    return pd;
  }

  /**
   * .
   * 得到PageData
   */
  public PageData getPageData(HttpServletRequest request) {
    PageData pd = new PageData(request);
    Object[] keys = pd.keySet().toArray();
    for (Object key : keys) {
      if ("".equals(pd.get(key.toString()))) {
        pd.remove(key.toString());
      }
    }
    return pd;
  }

  /**
   * .
   * 得到request对象
   */
  public HttpServletRequest getRequest() {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
            .getRequest();
    return request;
  }


  /**
   * @param
   * @return com.hzy.domain.Member
   * @作者 Chensb
   * @描述 TODO 获取当前登录的会员
   * @日期 2018/10/30 11:42
   */
  /*public Member getLoginMember() {
    PageData pd = this.getPageData();
    String token = pd.get("token") + "";
    if ("".equals(token)) {
      return null;
    }
    String session = redis.get(token);
    if (session == null || "".equals(session)) {
      return null;
    }
    Member member = (Member) redis.getObj(session);
    return member;
  }*/


  /**
   * @return
   * @方法名: startPage
   * @描述: TODO 开始分页
   * @返回类型: Page @修改人： @修改时间： @修改备注：
   */
  public Page startPage() {
    PageData pd = this.getPageData();
    Page page = this.startPage(pd.getCurrentPage(), pd.getPageSize());
    return page;
  }

  /**
   * @return com.github.pagehelper.Page
   * @Author Chensb
   * @Description TODO 开始分页
   * @Date 2018/8/30 17:56
   * @Param [pageno, pageSize]
   */
  public Page startPage(Integer pageNo, Integer pageSize) {
    if (pageNo == null || pageNo < 1) {
      pageNo = 1;
    }
    if (pageSize == null || pageSize < 1) {
      pageSize = 10;
    }
    Page page = PageHelper.startPage(pageNo, pageSize, true, false, false);
    return page;
  }

  /**
   * @param condition
   * @return
   * @方法名: getAndExample
   * @描述: TODO 获取条件，必须成对出现，例如 getAndExample("user_id=", 5, "sex=", "1");
   * @返回类型: Example @修改人： @修改时间： @修改备注：
   */
  public MyExample getAndExample(Class cls, Object... condition) {
    MyExample example = new MyExample(cls);
    MyExample.Criteria criteria = example.createCriteria();
    for (int i = 0; i < condition.length; i += 2) {
      if (isNull(condition[i].toString())) {
        continue;
      }
      criteria.andCondition(condition[i].toString(), condition[i + 1]);
    }
    example.setCriteria(criteria);
    return example;
  }

  /**
   * @param condition
   * @return
   * @方法名: getAndExample
   * @描述: TODO 获取条件，必须成对出现，例如 getAndExample("user_id=", 5, "sex=", "1");
   * @返回类型: Example @修改人： @修改时间： @修改备注：
   */
  public MyExample getOrExample(Class cls, Object... condition) {
    MyExample example = new MyExample(cls);
    MyExample.Criteria criteria = example.createCriteria();
    for (int i = 0; i < condition.length; i += 2) {
      criteria.orCondition(condition[i].toString(), condition[i + 1]);
    }
    example.setCriteria(criteria);
    return example;
  }

  /**
   * @param type_key 设置类别key
   * @return java.lang.String
   * @Author Chensb
   * @Description TODO 获取设置信息
   * @Date 2018/8/28 9:54
   */
  public Map<String, String> getSetting(String type_key) {
    Map<String, String> settings = (Map<String, String>) redis.getObj(type_key);
    if (settings == null) {
      settings = new HashMap<String, String>();
      List<HashMap<String, String>> list = settingsMapper.findByTypekey(type_key);
      for (HashMap<String, String> setting : list) {
        settings.put(setting.get("s_key"), setting.get("s_value"));
      }
      redis.setObj(type_key, settings);
    }
    return settings;
  }

  /**
   * @return java.lang.String
   * @Author Chensb
   * @Description TODO 获取设置信息
   * @Date 2018/8/28 9:54
   * @Param []
   */
  public String getSetting(String type_key, String setting_key) {
    String jsonObject = redis.get(type_key);
    if (isNull(jsonObject)) {
      JSONObject settings = new JSONObject();
      List<HashMap<String, String>> list = settingsMapper.findByTypekey(type_key);
      for (HashMap<String, String> setting : list) {
        settings.put(setting.get("s_key"), setting.get("s_value"));
      }
      redis.setObj(type_key, settings);
      return settings.get(setting_key) == null ? "" : settings.get(setting_key).toString();
    } else {
      JSONObject settings = JSONUtil.parseObj(jsonObject);
      return settings.get(setting_key) == null ? "" : settings.get(setting_key).toString();
    }
  }

  /**
   * @param typeKey    设置类别key
   * @param settingKey 设置详情key
   * @return java.lang.String
   * @Author Chensb
   * @Description TODO 获取单条设置详细信息
   * @Date 2018/8/28 9:54
   */
  public Setting getSettingInfo(String typeKey, String settingKey) {
    return settingsMapper.findOneByTypekey(new PageData("type_key", typeKey, "s_key", settingKey));
  }

  /**
   * @param path 图片原路径
   * @return
   * @方法名: upImgPath
   * @描述: TODO 替换图片路径
   * @返回类型: String @修改人： @修改时间： @修改备注：
   */
  public String upImgPath(String path) {
    if (path != null && path.startsWith("fs:")) {
      return path.replace("fs:", this.getSetting("sys_setting", "img_prefix"));
    } else if (path != null && path.startsWith("file_id:")) {
      return path.replace("file_id:", this.getSetting("sys_setting", "img_prefix"));
    } else if (path != null && path.indexOf("http://") == -1 && path.indexOf("https://") == -1) {
      return this.getSetting("sys_setting", "img_prefix") + path;
    }
    return path == null ? "" : path;
  }

  /**
   * @param map 数据
   * @param key 字段
   * @return
   * @方法名: upImgPath
   * @描述: TODO 替换图片路径
   * @返回类型: String @修改人： @修改时间： @修改备注：
   */
  public String upImgPath(Map map, String key) {
    if (map.get(key) == null) {
      return null;
    }
    String value = map.get(key) + "";
    if (value != null && value.startsWith("fs:")) {
      return value.replace("fs:", this.getSetting("sys_setting", "img_prefix"));
    } else if (value != null && value.startsWith("file_id:")) {
      return value.replace("file_id:", this.getSetting("sys_setting", "img_prefix"));
    } else if (value != null && value.indexOf("http://") == -1 && value.indexOf("https://") == -1) {
      return this.getSetting("sys_setting", "img_prefix") + value;
    } else {
      return value;
    }
  }

  /**
   * @param list 原数据列表路径
   * @param keys 图片字段列表
   * @return
   * @方法名: upImgPath
   * @描述: TODO 替换图片路径
   * @返回类型: String @修改人： @修改时间： @修改备注：
   */
  public List<Map> upImgPath(List<Map> list, String... keys) {
    for (Map map : list) {
      for (String key : keys) {
        map.put(key, upImgPath(map, key));
      }
    }
    return list;
  }

  /**
   * TODO 描述:根据图片ID获取图片地址
   *
   * @param imgId
   * @return java.lang.String
   * @author Chensb
   * @date 2019/11/18 16:07
   */
  /*public String getImgById(Integer imgId) {
    if (imgId != null) {
      Image image = imageMapper.selectByPrimaryKey(imgId);
      return this.upImgPath(image.getUrl());
    } else {
      return "";
    }
  }*/

  /**
   * TODO 描述:根据配置获取图片地址
   *
   * @param type 设置类别
   * @param key  设置key
   * @return java.lang.String
   * @author Chensb
   * @date 2019/11/18 16:07
   */
  /*public String getImgById(String type, String key) {
    String imgIds = this.getSetting(type, key);
    if (this.isInteger(imgIds)) {
      return this.getImgById(Integer.parseInt(imgIds));
    } else {
      return this.upImgPath(imgIds);
    }
  }*/

  /**
   * TODO 描述:判断是否为整数
   *
   * @param str 传入的字符串
   * @return boolean 是整数返回true,否则返回false
   * @author Chensb
   * @date 2019/11/18 16:16
   */
  public boolean isInteger(String str) {
    Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
    return pattern.matcher(str).matches();
  }

  /**
   * @param memberId 会员id
   * @return
   * @作者 Chensb
   * @描述 TODO 告诉前端api需更新会员缓存数据
   * @日期 2018/11/6 14:18
   */
  /*public Member updateMember(int memberId) {
    Member member = memberMapper.selectByPrimaryKey(memberId);
    member.setFace(this.upImgPath(member.getFace()));
    if (member.getOpenid() != null) {
      redis.setObjDay(member.getOpenid(), member, 365);
    }
    return member;
  }*/

  /**
   * @param openid 小程序openid
   * @return
   * @作者 Chensb
   * @描述 TODO 告诉前端api需更新会员缓存数据
   * @日期 2018/11/6 14:18
   */
  /*public Member updateMember(String openid) {
    Member member = memberMapper.selectOneByExample(this.getOrExample(Member.class, "openid=", openid, "unionid=", openid, "wxopenid=", openid));
    if (member != null) {
      member.setFace(this.upImgPath(member.getFace()));
      redis.setObjDay(member.getOpenid(), member, 365);
      redis.set("new_member_data_" + member.getMemberId(), "1");
    }
    return member;
  }*/

  /**
   * @param
   * @return java.lang.String
   * @作者 Chensb
   * @描述 TODO 获取请求来源
   * @日期 2018/11/15 11:05
   */
  protected String getSource() {
    String userAgent = this.getRequest().getHeader("user-agent").toLowerCase();
    String source = "";
    if (userAgent.indexOf("micromessenger") != -1) {
      //微信
      source = "微信";
    } else if (userAgent.indexOf("android") != -1 || userAgent.indexOf("okhttp") != -1) {
      //安卓
      source = "Android";
    } else if (userAgent.indexOf("iphone") != -1 || userAgent.indexOf("ipad") != -1 || userAgent.indexOf("ipod") != -1) {
      //苹果
      source = "IOS";
    } else {
      //电脑
      source = "电脑";
    }
    return source;
  }

  /**
   * TODO 描述:是否是APP端请求
   *
   * @param
   * @return boolean
   * @author Chensb
   * @date 2019/8/20 9:06
   */
  public boolean isApp() {
    String source = getSource();
    return "Android".equals(source) || "IOS".equals(source);
  }

  public RedisOperator getRedis() {
    return redis;
  }

  public void setRedis(RedisOperator redis) {
    this.redis = redis;
  }

  /**
   * @param
   * @return java.lang.St
   * @作者 Chensb
   * @描述 TODO 获取图片前缀设置
   * @日期 2018/11/22 14:08
   */
  public String getImgPrefix() {
    return this.getSetting("sys_setting", "img_prefix");
  }

  /**
   * 发送消息
   */
  /*public void sendMessage(MsgTemplate msg) {
    RedisQueue<MsgTemplate> redisQueue = redisTaskContainer.getRedisQueue();
    redisQueue.pushFromHead(msg);
    this.getRedis().sendMessage("message", "1"); // 给各模块发送消息，告诉他们去队列那消息处理回调
  }
*/

  /**
   * @param
   * @return boolean
   * @作者 Chensb
   * @描述 TODO 是否是测试模式
   * @日期 2019/1/15 12:59
   */
  public boolean isTestModel() {
    String model = this.getSetting("sys_setting", "test_model");
    return "1".equals(model);
  }

  /**
   * TODO 描述:字符串是否为空
   *
   * @param s
   * @return boolean
   * @author Chensb
   * @date 2019/8/28 15:44
   */
  public boolean isNull(String s) {
    return s == null || "".equals(s);
  }

  /**
   * @param
   * @return java.lang.String
   * @作者 Chensb
   * @描述 TODO 获取请求IP地址
   * @日期 2018/12/6 19:29
   */
  public static String getIp() {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    String ip = "";
    if (request.getHeader("x-forwarded-for") == null) {
      ip = request.getRemoteAddr();
    } else {
      ip = request.getHeader("x-forwarded-for");
    }
    return ip;
  }
}
