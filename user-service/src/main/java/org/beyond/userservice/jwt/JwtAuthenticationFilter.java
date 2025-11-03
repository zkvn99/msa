package org.beyond.userservice.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String token = getJwtFromRequest(request);

    if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
      // 1. token에 담긴 값을 이용해서 Authentication 설정
      // 2. DB에서 조회한 뒤 Authentication 설정
      String username = jwtTokenProvider.getUsernameFromJWT(token);
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      UsernamePasswordAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities()
          );
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    // if 문을 통과하지 못했다면 SecurityContextHolder의 Authentication이 설정
    // 통과하지 않았다면 해당 값이 비어있는 상태로 다음 필터의 진행으로 넘어감
    // 이어지는 필터에서 인증 성공 or 실패가 가려짐
    filterChain.doFilter(request, response);
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
