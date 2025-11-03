package com.ohgiraffers.userservice.command.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    // 추가: UUID(공개용 식별자)
    @Column(unique = true, nullable = false, updatable = false, length = 36)
    private String publicId;

    @PrePersist
    private void assignUuid() {
        if (publicId == null) {
            publicId = java.util.UUID.randomUUID().toString();
        }
    }

    public void setEncodedPassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
