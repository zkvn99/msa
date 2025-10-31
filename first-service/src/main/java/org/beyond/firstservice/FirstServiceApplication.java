package org.beyond.firstservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient  // 해당 어플리케이션을 서비스 발견 클라이언트로 설정하는 어노테이션
public class FirstServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(FirstServiceApplication.class, args);
  }

}
