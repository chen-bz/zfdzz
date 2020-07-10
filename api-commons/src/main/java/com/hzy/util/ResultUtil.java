package com.hzy.util;

import cn.hutool.json.JSONObject;
import com.github.pagehelper.Page;
import java.util.Map;

public class ResultUtil {

	/**
	 * @param message 成功提示信息
	 * @return
	 * @方法名: success
	 * TODO 返回成功
	 * @返回类型: Object @修改人： @修改时间： @修改备注：
	 */
	public static Map success(String message) {
		JSONObject map = new JSONObject();
		JSONObject result = new JSONObject();
		map.put("status", 1);
		map.put("result", result);
		map.put("message", message);
		return map;
	}

	/**
	 * @param message 失败提示信息
	 * @return
	 * @方法名: fail
	 * TODO 返回失败
	 * @返回类型: Object @修改人： @修改时间： @修改备注：
	 */
	public static Map fail(String message) {
		JSONObject map = new JSONObject();
		JSONObject result = new JSONObject();
		map.put("status", 0);
		map.put("result", result);
		map.put("message", message);
		return map;
	}

	/**
	 * @param message 失败提示信息
	 * @param result  失败返回的数据
	 * @return
	 * @方法名: fail
	 * TODO 返回失败
	 * @返回类型: Object @修改人： @修改时间： @修改备注：
	 */
	public static Map fail(String message, Object result) {
		JSONObject map = new JSONObject();
		map.put("status", 0);
		map.put("result", result);
		map.put("message", message);
		return map;
	}

	/**
	 * @return
	 * @方法名: unLogin
	 * TODO 未登录
	 * @返回类型: Object @修改人： @修改时间： @修改备注：
	 */
	public static Map unLogin(String... message) {
		JSONObject map = new JSONObject();
		JSONObject result = new JSONObject();
		map.put("status", 2);
		map.put("result", result);
		map.put("message", message.length > 0 ? message[0] : "请先登录");
		return map;
	}

	/**
	 * @param message 成功提示信息
	 * @param result  成功返回数据
	 * @return
	 * @方法名: getResult
	 * TODO 返回成功数据
	 * @返回类型: Object @修改人： @修改时间： @修改备注：
	 */
	public static Map getResult(String message, Object result) {
		JSONObject map = new JSONObject();
		map.put("status", 1);
		map.put("result", result);
		map.put("message", message);
		return map;
	}

	/**
	 * @param result 成功返回数据
	 * @return
	 * @方法名: getResult
	 * TODO 返回成功数据
	 * @返回类型: Object @修改人： @修改时间： @修改备注：
	 */
	public static Map getResult(Object result) {
		JSONObject map = new JSONObject();
		map.put("status", 1);
		map.put("result", result);
		map.put("message", "请求成功");
		return map;
	}

	/**
	 * @param message     成功提示信息
	 * @param result
	 * @param currentPage
	 * @param totalPage
	 * @param totalResult
	 * @return
	 * @方法名: getResult
	 * TODO 返回分页数据
	 * @返回类型: Object
	 * @修改人：
	 * @修改时间：
	 * @修改备注：
	 */
	public static Map getResult(String message, Object result, Integer currentPage, Integer totalPage,
	                            Long totalResult) {
		JSONObject map = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("currentPage", currentPage);
		data.put("totalPage", totalPage);
		data.put("totalResult", totalResult);
		data.put("data", result);
		map.put("status", 1);
		map.put("result", new PageData("pageDate", data));
		map.put("message", message);
		return map;
	}

	/**
	 * TODO 获取分页数据
	 *
	 * @param message
	 * @param result
	 * @return
	 * @方法名: getPageResult
	 * @返回类型: Object
	 * @添加人：Chensb
	 * @添加时间：2018年8月7日 下午5:03:17
	 * @修改备注：
	 */
	public static Map getPageResult(String message, Page result) {
		JSONObject map = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("currentPage", result.getPageNum());
		data.put("totalPage", result.getPages());
		data.put("totalResult", result.getTotal());
		data.put("data", result);
		map.put("status", 1);
		map.put("result", new PageData("pageDate", data));
		map.put("message", message);
		return map;
	}

	/**
	 * TODO 获取分页数据
	 *
	 * @param result 分页数据
	 * @return
	 * @方法名: getPageResult
	 * @返回类型: Object
	 * @添加人：Chensb
	 * @添加时间：2018年8月7日 下午5:03:17
	 * @修改备注：
	 */
	public static Map getPageResult(Page result) {
		JSONObject map = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("currentPage", result.getPageNum());
		data.put("totalPage", result.getPages());
		data.put("totalResult", result.getTotal());
		data.put("data", result);
		map.put("status", 1);
		map.put("result", new PageData("pageDate", data));
		map.put("message", "请求成功");
		return map;
	}
}
