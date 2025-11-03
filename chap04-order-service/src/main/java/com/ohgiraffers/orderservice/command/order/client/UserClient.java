package com.ohgiraffers.orderservice.command.order.client;

import com.ohgiraffers.orderservice.common.ApiResponse;
import com.ohgiraffers.orderservice.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/* 1. API Gateway 호출*/
@FeignClient(name = "user-service", url = "http://localhost:8000", configuration = FeignClientConfig.class)
/* 2. 직접 user-service 호출 */
//@FeignClient(name = "swcamp-user-service",  configuration = FeignClientConfig.class)
public interface UserClient {

    /* UserService 에서 사용자 상태나 간단한 정보를 조회하는 API */
    @GetMapping("/api/v1/user-service/users/{userId}/grade")
//    @GetMapping("/users/{userId}/grade")
    ApiResponse<String> getUserGrade(@PathVariable("userId") Long userId);
}
