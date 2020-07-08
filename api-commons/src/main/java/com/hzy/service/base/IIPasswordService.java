package com.hzy.service.base;

import cn.hutool.crypto.SecureUtil;
import org.springframework.stereotype.Service;

@Service
public class IIPasswordService extends BaseService {

  /**
   * 字符串反转
   *
   * @param s 字符串
   * @return String
   */
  public static String inversion(String s) {
    StringBuffer newStr = new StringBuffer();
    for (int i = s.length() - 1; i >= 0; i--) {
      newStr.append(s.charAt(i));
    }
    return newStr.toString();
  }

  /**
   * 加密
   *
   * @param password 一级加密
   * @return 加密过的密码
   */
  public static String encryption(String password) {
    password = SecureUtil.md5(password);
    password = SecureUtil.md5("/.,HZY" + inversion(password) + "hzy,./");
    return password;
  }

  /**
   * 验证密码
   *
   * @param password 用户输入的密码
   * @param PASSWORD 用户设置的密码
   * @return
   */
  public static boolean verify(String password, String PASSWORD) {
    return PASSWORD != null && password != null && PASSWORD.equals(encryption(password));
  }

}
