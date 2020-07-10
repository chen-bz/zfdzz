package com.hzy.service.api.impl;

import com.hzy.service.api.ApiService;
import com.hzy.service.base.BaseService;
import com.hzy.util.ResultUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**.
 * @ClassName ApiServiceImpl
 * @Description TODO
 * @Author Chensb
 * @Date 2018/8/30 17:45
 * @Version 1.0
 */
@Service
public class ApiServiceImpl extends BaseService implements ApiService {

  @Override
  @Transactional(propagation = Propagation.SUPPORTS)
  public Object index() {
    return ResultUtil.getResult("请求成功", null);
  }

  @Override
  @Transactional(propagation = Propagation.SUPPORTS)
  public Object appVersion(Integer versionCode) {
    //**********业务逻辑**********
    return ResultUtil.getResult("已是最新版本", null);
  }

}
