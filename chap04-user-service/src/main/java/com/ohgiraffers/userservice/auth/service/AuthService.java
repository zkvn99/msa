package com.ohgiraffers.userservice.auth.service;

import com.ohgiraffers.userservice.auth.dto.LoginRequest;
import com.ohgiraffers.userservice.auth.dto.TokenResponse;
import com.ohgiraffers.userservice.auth.entity.RefreshToken;
import com.ohgiraffers.userservice.auth.repository.RefreshTokenRepository;
import com.ohgiraffers.userservice.command.entity.User;
import com.ohgiraffers.userservice.command.repository.UserRepository;
import com.ohgiraffers.userservice.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("올바르지 않은 아이디 혹은 비밀번호"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("올바르지 않은 아이디 혹은 비밀번호");
        }

        // sub = publicId(UUID)
        String publicId = user.getPublicId();
        String role = user.getRole().name();

        String accessToken = jwtTokenProvider.createAccessToken(publicId, role);
        String refreshToken = jwtTokenProvider.createRefreshToken(publicId, role);

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .publicId(publicId)            // 키는 publicId
                        .token(refreshToken)
                        .expiryDate(new Date(System.currentTimeMillis() + jwtTokenProvider.getRefreshExpiration()))
                        .build()
        );

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public TokenResponse refreshToken(String refreshToken) {
        // 1) 검증
        jwtTokenProvider.validateToken(refreshToken);
        // 2) sub에서 publicId 추출
        String publicId = jwtTokenProvider.getPublicIdFromJWT(refreshToken);

        // 3) 서버 저장 리프레시 토큰과 일치 검사
        RefreshToken stored = refreshTokenRepository.findById(publicId)
                .orElseThrow(() -> new RuntimeException("해당 유저 리프레시 토큰 없음"));
        if (!stored.getToken().equals(refreshToken)) {
            throw new RuntimeException("리프레시 토큰 불일치");
        }

        // 4) 신규 토큰 재발급 (role은 DB에서 조회)
        User user = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("해당 유저 찾지 못함"));

        String newAccess = jwtTokenProvider.createAccessToken(publicId, user.getRole().name());
        String newRefresh = jwtTokenProvider.createRefreshToken(publicId, user.getRole().name());

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .publicId(publicId)
                        .token(newRefresh)
                        .expiryDate(new Date(System.currentTimeMillis() + jwtTokenProvider.getRefreshExpiration()))
                        .build()
        );

        return TokenResponse.builder()
                .accessToken(newAccess)
                .refreshToken(newRefresh)
                .build();
    }

    @Transactional
    public void logout(String userIdFromPrincipal) {
        // principal에는 게이트웨이가 주입한 PK(숫자)가 들어있음
        Long pk = Long.valueOf(userIdFromPrincipal);
        User user = userRepository.findById(pk)
                .orElseThrow(() -> new RuntimeException("해당 유저 찾지 못함"));

        // publicId 기준으로 리프레시 토큰 삭제
        refreshTokenRepository.deleteById(user.getPublicId());
    }
}