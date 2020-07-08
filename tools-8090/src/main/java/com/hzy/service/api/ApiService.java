package com.hzy.service.api;

/**.
 * @ClassName ApiService
 * @Description TODO
 * @Author Chensb
 * @Date 2018/8/30 17:44
 * @Version 1.0
 */
public interface ApiService {
  /**.
   * @return java.lang.Object
   * @Author Chensb
   * @Description TODO 首页数据
   * @Date 2018/9/11 13:51
   * @Param []
   */
  public Object index();

  /**.
   * @return java.lang.Object
   * @Author Chensb
   * @Description TODO APP版本检测
   * @Date 2018/9/11 13:51
   * @Param [version_code]
   */
  public Object appVersion(Integer versionCode);

}
