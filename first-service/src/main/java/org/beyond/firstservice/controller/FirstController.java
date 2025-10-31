package org.beyond.firstservice.controller;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstController {

  private Environment environment;

  public FirstController(Environment environment) {
    this.environment = environment;
  }

  /* 스케일 아웃 된 상황에서 로드밸런싱 되는 모습 확인을 위한 엔드포인트 */
  @GetMapping("/health")
  public String healthCheck() {
    return "First Service is OK. Port = " + environment.getProperty("local.server.port");
  }

  /* API Gateway에서 요청을 가공해서 전달하는 모습 확인을 위한 엔드포인트 */
  @GetMapping("/message")
  public String message(@RequestHeader("first-request") String header) {
    return "first-request header : " + header;
  }
}
