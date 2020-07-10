package com.hzy.controller;

import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
  @Resource
  private RestTemplate restTemplate;
  @Value("${service-url.tools-service}")
  private String serverURL;

  @GetMapping("/demo/nacos/{id}")
  public String getPayment(@PathVariable("id") Integer id) {
    return "nacos register, serverPort" + port +"\t id" +id;
  }

  @GetMapping(value = "/consumer/demo/nacos/{id}")
  public String paymentInfo(@PathVariable("id") Integer id) {
    return restTemplate.getForObject(serverURL + "/demo/nacos/" + id, String.class);
  }

}
