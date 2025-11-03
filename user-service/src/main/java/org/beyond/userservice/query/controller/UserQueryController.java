package org.beyond.userservice.query.controller;

import lombok.RequiredArgsConstructor;

import org.beyond.userservice.common.dto.ApiResponse;
import org.beyond.userservice.query.dto.UserDetailResponse;
import org.beyond.userservice.query.dto.UserListResponse;
import org.beyond.userservice.query.service.UserQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserQueryController {

  private final UserQueryService userQueryService;

  @GetMapping("/users/me")
  public ResponseEntity<ApiResponse<UserDetailResponse>> getUserDetail(
      // 인증 필터를 거치고 나면 Spring Security Context에 인증 객체가 저장 되어 있음
      // 필요하다면 UserDetails <- Users <- CustomUser의 형태로 상속(확장) 해서
      // id, pwd, authorities 외의 정보도 담아서 사용할 수 있음
      @AuthenticationPrincipal UserDetails userDetails
  ) {

    UserDetailResponse response = userQueryService.getUserDetail(userDetails.getUsername());
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  // 간단한 URL 패턴보다 복잡한 비즈니스 로직이나 메서드 단위의 세밀한 보안 제어가 필요한 경우
  // @PreAuthorize() : 메소드 호출 전 조건 평가
  // @PostAuthorize() : 메소드 호출 후 반환 결과에 기반한 처리 가능
  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/admin/users")
  public ResponseEntity<ApiResponse<UserListResponse>> getUsers() {
    UserListResponse response = userQueryService.getAllUsers();
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /* SpEL(Spring Expression Language) 문법 예시

    EX 1. 파라미터 userId와 현재 로그인한 username(authentication.name) 비교
    @PreAuthorize("#userId == authentication.name or hasAuthority('ADMIN')")

    EX 2. 서비스 로직 실행은 되지만, 반환 후에 owner가 현재 사용자와 다르면 403 발생
    @PostAuthorize("returnObject.body.data.owner == authentication.name or hasAuthority('ADMIN')")

    EX 3. @Component("userAuth") 로 등록된 Bean의 canAccess 메서드 호출
    public boolean canAccess(String targetId, Authentication auth)
    @PreAuthorize("@userAuth.canAccess(#userId, authentication)")
   */

}
