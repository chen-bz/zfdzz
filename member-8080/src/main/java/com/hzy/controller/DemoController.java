package com.hzy.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName PaymentController
 * @Description TODO
 * @Author Chensb
 * @Date 2020/7/7 10:05
 * @Version 1.0
 */
@RestController
public class DemoController {

  @Value("${server.port}")
  private String port;

  @GetMapping("/demo/nacos/{id}")
  public String getPayment(@PathVariable("id") Integer id) {
    return "nacos register, serverPort" + port +"\t id" +id;
  }

}
