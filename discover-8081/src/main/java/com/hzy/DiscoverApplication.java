package com.hzy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * @ClassName DiscoverApplication
 * @Description TODO
 * @Author Chensb
 * @Date 2020/7/7 15:09
 * @Version 1.0
 */
@EnableDiscoveryClient
@EnableFeignClients // 启用feign
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) //取消数据源的自动创建
public class DiscoverApplication {

  public static void main(String[] args) {
    SpringApplication.run(DiscoverApplication.class, args);
  }
}
