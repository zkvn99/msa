package com.ohgiraffers.orderservice.config;

import feign.RequestInterceptor;
import jakarta.servlet.ServletRequestAttributeEvent;
import org.apache.http.HttpHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            /* 현재 요청의 request를 가져옴 */
            ServletRequestAttributes requestAttributes
                    = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if(requestAttributes != null) {
                /* 1. Gateway로 요청하는 상황 -> 토큰을 담고 요청 */
                String authorizationHeader = requestAttributes
                        .getRequest()
                        .getHeader(HttpHeaders.AUTHORIZATION);
                if(authorizationHeader != null) requestTemplate.header(
                        HttpHeaders.AUTHORIZATION, authorizationHeader
                );

                /* 2. 직접 user-service 요청하는 상황 -> 헤더 정보 담고 요청 */
/*                String userId = requestAttributes.getRequest().getHeader("X-User-Id");
                String role = requestAttributes.getRequest().getHeader("X-User-Role");
                requestTemplate.header("X-User-Id", userId);
                requestTemplate.header("X-User-Role", role);*/
            }

        };
    }
}
