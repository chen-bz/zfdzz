package com.hzy.service;

import com.hzy.service.base.BaseService;
import org.springframework.stereotype.Service;

/**
 * @ClassName DemoService
 * @Description TODO
 * @Author Chensb
 * @Date 2020/7/8 16:18
 * @Version 1.0
 */
@Service
public class DemoService extends BaseService {

  public String testBaseService() {
    return this.getSetting("sys_setting", "domain_name");
  }
}
