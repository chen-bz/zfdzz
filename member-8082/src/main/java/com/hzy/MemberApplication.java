package com.hzy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName DiscoverApplication
 * @Description TODO
 * @Author Chensb
 * @Date 2020/7/7 15:09
 * @Version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class MemberApplication {

  public static void main(String[] args) {
    SpringApplication.run(MemberApplication.class, args);
  }
}
