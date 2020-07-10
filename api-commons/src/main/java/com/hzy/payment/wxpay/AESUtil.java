/**
 *
 */
package com.hzy.payment.wxpay;

import com.hzy.util.MD5;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author jing.huang
 * @function
 * @date 2018年1月10日
 */
public class AESUtil {
	/**
	 * 密钥算法
	 */
	private static final String ALGORITHM = "AES";
	/**
	 * 加解密算法/工作模式/填充方式
	 */
	private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS5Padding";
	/**
	 * 生成key
	 */
	private static SecretKeySpec key;

	/**
	 * AES加密
	 *
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String encryptData(String data, String keys) throws Exception {
		if (key == null) {
			key = new SecretKeySpec(MD5.md5(keys).toLowerCase().getBytes(), ALGORITHM);
		}
		// 创建密码器
		Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
		// 初始化
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return Base64Util.encode(cipher.doFinal(data.getBytes()));
	}

	/**
	 * AES解密
	 *
	 * @param base64Data
	 * @return
	 * @throws Exception
	 */
	public static String decryptData(String base64Data, String keys) throws Exception {
		if (key == null) {
			key = new SecretKeySpec(MD5.md5(keys).toLowerCase().getBytes(), ALGORITHM);
		}
		Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
		cipher.init(Cipher.DECRYPT_MODE, key);
		return new String(cipher.doFinal(Base64Util.decode(base64Data)));
	}

}