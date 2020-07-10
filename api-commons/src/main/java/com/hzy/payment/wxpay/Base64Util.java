/**
 *
 */
package com.hzy.payment.wxpay;

import java.util.Base64;

/**
 * @author jing.huang
 * @function jdk8支持
 * @date 2018年1月10日
 */
public class Base64Util {

	/**
	 * TODO 描述:解码
	 *
	 * @param encodedText
	 * @return byte[]
	 * @author Chensb
	 * @date 2019/10/23 15:17
	 */
	public static byte[] decode(String encodedText) {
		final Base64.Decoder decoder = Base64.getDecoder();
		return decoder.decode(encodedText);
	}

	/**
	 * TODO 描述:编码
	 *
	 * @param data
	 * @return java.lang.String
	 * @author Chensb
	 * @date 2019/10/23 15:17
	 */
	public static String encode(byte[] data) {
		final Base64.Encoder encoder = Base64.getEncoder();
		return encoder.encodeToString(data);
	}

}