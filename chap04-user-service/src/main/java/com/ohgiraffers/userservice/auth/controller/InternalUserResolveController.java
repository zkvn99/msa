package com.ohgiraffers.userservice.auth.controller;

import com.ohgiraffers.userservice.command.entity.User;
import com.ohgiraffers.userservice.command.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/users")
public class InternalUserResolveController {

    private final UserRepository userRepository;

    // 게이트웨이만 호출할 수 있게 간단한 보호 장치
    private static final String INTERNAL_SECRET = "gateway-secret";

    @GetMapping("/resolve")
    public Map<String,Object> resolve(
            @RequestParam("publicId") String publicId,
            @RequestHeader(value = "X-Internal-Secret", required = false) String secret
    ) {
        if (!INTERNAL_SECRET.equals(secret)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }

        User user = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return Map.of(
                "userId", user.getId(),
                "role", user.getRole().name()
        );
    }
}
