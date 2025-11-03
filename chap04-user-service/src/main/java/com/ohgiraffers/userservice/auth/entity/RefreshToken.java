package com.ohgiraffers.userservice.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken {

    @Id
    private String publicId;   // 키를 username -> publicId 로 변경

    private String token;
    private Date expiryDate;

    @Builder
    public RefreshToken(String publicId, String token, Date expiryDate) {
        this.publicId = publicId;
        this.token = token;
        this.expiryDate = expiryDate;
    }
}
