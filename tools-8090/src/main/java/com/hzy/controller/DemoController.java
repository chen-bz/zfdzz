package com.hzy.controller;

import com.hzy.service.DemoService;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
public class DemoController {

  @Value("${server.port}")
  private String port;
  @Resource
  private DemoService demoService;
  @Resource
  private RestTemplate restTemplate;
  @Value("${service-url.member-service}")
  private String memberServerURL;

  @GetMapping("/demo/nacos/{id}")
  public String getPayment(@PathVariable("id") Integer id) {
    return "nacos register, serverPort" + port +"\t id" +id;
  }

  @GetMapping("/demo/getSetting")
  public String getSetting() {
    return demoService.testBaseService();
  }
  @GetMapping("/demo/memberServerURL")
  public String memberServerURL() {
    return memberServerURL;
  }
  @GetMapping(value = "/member/demo/nacos/{id}")
  public String paymentInfo(@PathVariable("id") Integer id) {
    String str = "";
    if (str.isEmpty()) {

    }
    return restTemplate.getForObject(memberServerURL + "/demo/nacos/" + id, String.class);
  }
}
