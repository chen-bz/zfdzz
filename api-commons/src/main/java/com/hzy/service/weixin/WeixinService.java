package com.hzy.service.weixin;

import com.hzy.domain.MsgTemplate;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

public interface WeixinService {

	/**
	 * TODO 描述:获取接口调用凭证
	 *
	 * @param
	 * @return java.lang.String
	 * @author Chensb
	 * @date 2019/10/16 11:26
	 */
	String getAccessToken();

	/**
	 * TODO 描述:获取小程序码
	 *
	 * @param scene 小程序码值:
	 *               最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~，
	 *               其它字符请自行编码为合法字符（因不支持%，中文无法使用 urlencode 处理，请使用其他编码方式）
	 * @param page 跳转页面:必须是已经发布的小程序存在的页面（否则报错），
	 *              例如 pages/index/index, 根路径前不要填加 /,不能携带参数（参数请放在scene字段里），
	 *              如果不填写这个字段，默认跳主页面
	 * @param savePath 小程序码保存硬盘路径
	 * @param fileName 小程序码图片名称
	 * @return java.lang.String
	 * @author Chensb
	 * @date 2019/10/16 11:44
	 */
	String getUnlimited(String scene, String page, String savePath, String fileName);

	/**
	 * TODO 描述:获取小程序码
	 *
	 * @param scene 小程序码值:
	 *               最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~，
	 *               其它字符请自行编码为合法字符（因不支持%，中文无法使用 urlencode 处理，请使用其他编码方式）
	 * @param page 跳转页面:必须是已经发布的小程序存在的页面（否则报错），
	 *              例如 pages/index/index, 根路径前不要填加 /,不能携带参数（参数请放在scene字段里），
	 *              如果不填写这个字段，默认跳主页面
	 * @return java.lang.String
	 * @author Chensb
	 * @date 2019/10/16 11:44
	 */
	String getUnlimited(String scene, String page, HttpServletResponse response);

	/**
	 * TODO 描述:发送客服消息
	 *
	 * @param openid
	 * @return void
	 * @author Chensb
	 * @date 2019/12/3 9:50
	 */
	void sendServiceMsg(String openid, String text, String url) throws IOException;

	/**
	 * TODO 描述:发送小程序统一服务消息
	 *
	 * @param touser openID
	 * @param template_id 模板ID
	 * @param page 小程序路径
	 * @param data 小程序模板数据
	 * @return void
	 * @author Chensb
	 * @date 2020/1/7 11:34
	 */
	void uniformSend(String touser, String template_id, String page, String data);

	/**
	 * TODO 描述:检查是否关注公众号
	 *
	 * @param openid 公众号授权获取的openID
	 * @return int
	 * @author Chensb
	 * @date 2020/4/8 15:45
	 */
	void updateUserIfSubscribe(String openid, HttpServletResponse response);

	/**
	 * TODO 描述:检查用户是否关注微信公众号
	 *
	 * @param
	 * @return java.util.Map
	 * @author Chensb
	 * @date 2020/4/13 13:46
	 */
	Map checkMemberIfSubscribe(HttpServletResponse response);

	/**
	 * TODO 描述:微信授权登录回调
	 *
	 * @param code
	 * @param state
	 * @param response
	 * @return void
	 * @author Chensb
	 * @date 2020/4/13 13:55
	 */
	void wxLoginRedirect(String code, String state, HttpServletResponse response);

	/**
	 * TODO 描述:httpGet请求
	 *
	 * @param url
	 * @return java.lang.String
	 * @author Chensb
	 * @date 2019/12/9 14:16
	 */
	String httpGet(String url);

	/**
	 * TODO 描述:httpPost请求
	 *
	 * @param url
	 * @return java.lang.String
	 * @author Chensb
	 * @date 2019/12/9 14:16
	 */
	String httpPost(String url, String param) throws IOException;


	/**
	 * @作者 Chensb
	 * @描述 TODO 发送模板消息
	 * @日期 2018/12/13 17:31
	 *
	 * @param template 参数
	 * @return java.lang.Object
	 */
	Object sendTemplateMsg(MsgTemplate template);

	/**
	 * TODO 描述:获取公众号accessToken
	 *
	 * @param
	 * @return java.lang.String
	 * @author Chensb
	 * @date 2020/4/23 14:46
	 */
	String getWeixinAccessToken();

	/**
	 * @作者 suyixiang
	 * @描述 TODO 获取微信分享
	 * @日期 2018/12/13 17:57
	 * @param
	 * @return java.lang.String
	 */
	String getWXJsapiTicket();
}
