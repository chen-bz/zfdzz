package com.hzy.service.sms;

import java.util.Map;

/**
 * @ClassName SmsService
 * @Description TODO
 * @Author Chensb
 * @Date 2020/3/16 14:50
 * @Version 1.0
 */
public interface SmsService {

	/**
	 * TODO 描述:发送短信逻辑
	 *
	 * @param phone 手机号
	 * @param code 验证码
	 * @param codeKey 类别key
	 * @param templateid 模板ID
	 * @param ipAstrict 是否限制条数
	 * @return java.util.Map
	 * @author Chensb
	 * @date 2020/3/16 16:07
	 */
	Map send(String phone, String code, String codeKey, String templateid, boolean ipAstrict);

	/**
	 * TODO 描述:绑定手机号发送短信
	 *
	 * @param phone 手机号
	 * @return java.util.Map
	 * @author Chensb
	 * @date 2020/3/16 16:14
	 */
	Map sendByBindPhone(String phone);


}
