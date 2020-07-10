package com.hzy.service.demo.impl;

import com.hzy.service.demo.DemoService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;

/**
 * @ClassName DemoServiceImpl
 * @Description TODO
 * @Author Chensb
 * @Date 2020/7/10 15:21
 * @Version 1.0
 */
@Service
public class DemoServiceImpl implements DemoService {

  @Override
  @GlobalTransactional(
          name = "fsp-create-order", // 事务名称
          rollbackFor = Exception.class // 拦截的异常
  )
  public String testTransactional() {
    return null;
  }
}
