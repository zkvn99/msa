package org.beyond.userservice.config;

import lombok.RequiredArgsConstructor;
import org.beyond.userservice.jwt.JwtAuthenticationFilter;
import org.beyond.userservice.jwt.JwtTokenProvider;
import org.beyond.userservice.jwt.RestAccessDeniedHandler;
import org.beyond.userservice.jwt.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity // 메소드 레벨의 인증 처리 활성화
public class SecurityConfig {

  private final JwtTokenProvider jwtTokenProvider;
  private final UserDetailsService userDetailsService;
  private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
  private final RestAccessDeniedHandler restAccessDeniedHandler;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // CSRF 처리 비활성화 (default가 활성화이므로 작성)
    http.csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS
            )
        )
        // 요청 http method, url 기준으로 인증/인가 필요 여부 설정
        .authorizeHttpRequests(
            auth -> auth.requestMatchers(
                HttpMethod.POST, "/api/v1/users", "/api/v1/auth/login", "/api/v1/auth/refresh"
            ).permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/users/me")
                .hasAuthority("USER")
                .anyRequest().authenticated()
        )
        // 커스텀 인증 필터 추가 (JWT 토큰 사용하여 확인)
        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        // 인증, 인가 실패 시 핸들링
        .exceptionHandling(
            exception ->
                //인증 실패 핸들러
                exception.authenticationEntryPoint(restAuthenticationEntryPoint)
                         //인가 실패 핸들러
                          .accessDeniedHandler(restAccessDeniedHandler)
        );

    return http.build();
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);
  }
}
