package com.hzy.service.sms.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.hzy.service.base.BaseService;
import com.hzy.service.sms.SmsService;
import com.hzy.utils.DateUtil;
import com.hzy.utils.PageData;
import com.hzy.utils.ResultUtil;
import com.hzy.utils.SendMessageUtil;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
 * @ClassName SmsServiceImpl
 * @Description TODO
 * @Author Chensb
 * @Date 2020/3/16 15:00
 * @Version 1.0
 */
@Service
public class SmsServiceImpl extends BaseService implements SmsService {


	@Override
	public Map send(String phone, String code, String codeKey, String templateid, boolean ipAstrict) {
		if (this.getSetting("sms_setting", "disabled_phone").indexOf(phone) != -1) {
			return null;
		}
		if (redis.get(phone) != null) {
			return ResultUtil.fail("发送频繁,请稍后重试");
		}
		String ip = this.getIp(this.getRequest()) + "_" + DateUtil.getDays();
		String countKey = phone + "_" + DateUtil.getDays();
		Integer phoneCount = Integer.parseInt(redis.get(countKey) == null ? "0" : redis.get(countKey));
		Integer ipCount = Integer.parseInt(redis.get(ip) == null ? "0" : redis.get(ip));
		if (phoneCount >= 10 && (ipAstrict && ipCount >= 40)) {
			return ResultUtil.fail("抱歉,您今日发送短信条数超过限制");
		}
		phoneCount++;
		ipCount++;
		// 阿里大于短信发送
		Map result = SendMessageUtil.sendSms(phone, templateid, JSONUtil.parseFromMap(new PageData("code", code)).toString());
		redis.setDay(countKey, phoneCount.toString(), 1);
		redis.setDay(ip, ipCount.toString(), 1);
		redis.setMinutes(phone, phone, 1);
		redis.setMinutes(codeKey, code, 5); // 放入缓存
		return result;
	}

	/**
	 * @作者 Chensb
	 * @描述 TODO 获取请求IP
	 * @日期 2018/11/6 16:16
	 *
	 * @param request 请求
	 * @return java.lang.String
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = "";
		if (request.getHeader("x-forwarded-for") == null) {
			ip = request.getRemoteAddr();
		} else {
			ip = request.getHeader("x-forwarded-for");
		}
		return ip;
	}

	/**
	 * TODO 描述:绑定手机号发送短信
	 *
	 * @param phone 手机号
	 * @return java.util.Map
	 * @author Chensb
	 * @date 2020/3/16 16:14
	 */
	@Override
	public Map sendByBindPhone(String phone) {
		/*if (memberMapper.selectCountByExample(this.getAndExample(Member.class, "phone=", phone)) > 0) {
			return ResultUtil.fail("手机号已被绑定");
		}*/
		String codeKey = "bind_phone_" + phone;
		if (redis.ttl(codeKey) > 240) {
			return ResultUtil.fail("操作频繁, 请稍后再试");
		}
		String code = "111111";
		if (isTestModel()) {
			redis.setMinutes(codeKey, code, 5); // 放入缓存
			return ResultUtil.success("发送成功，测试模式验证码【111111】");
		}
		code = RandomUtil.randomNumbers(6);
		// 发送短信逻辑,看用的是什么平台
		return send(phone, code, codeKey, this.getSetting("sms_setting", "temp_bind_phone_code"), false);
	}
}
