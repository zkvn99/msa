package org.beyond.userservice.auth.controller;

import lombok.RequiredArgsConstructor;

import org.beyond.userservice.auth.dto.LoginRequest;
import org.beyond.userservice.auth.dto.RefreshTokenRequest;
import org.beyond.userservice.auth.dto.TokenResponse;
import org.beyond.userservice.auth.service.AuthService;
import org.beyond.userservice.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest request) {
    TokenResponse response = authService.login(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @PostMapping("/refresh")
  public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(
      @RequestBody RefreshTokenRequest request
  ) {
    TokenResponse response = authService.refreshToken(request.getRefreshToken());
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @PostMapping("/logout")
  public ResponseEntity<ApiResponse<Void>> logout(
      @AuthenticationPrincipal UserDetails userDetails
  ) {
    authService.logout(userDetails.getUsername());
    return ResponseEntity.ok(ApiResponse.success(null));
  }
}
