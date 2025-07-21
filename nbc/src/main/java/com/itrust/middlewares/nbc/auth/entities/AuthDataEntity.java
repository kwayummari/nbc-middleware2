package com.itrust.middlewares.nbc.auth.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@NoArgsConstructor
@Setter
@Getter
@Table(name = "auth_data")
public class AuthDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @Column(name = "timestamp")
    private String timestamp;

    @Column(name = "token", columnDefinition = "TEXT")
    private String token;

    @Column(name = "username")
    private String username;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "scope")
    private String scope;

    @Column(name = "grant_type")
    private String grantType;

    @Column(name = "password")
    private String password;

    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refreshToken;

    private String channel;

    private String signature;

    private String terminalId;

    private String thirdpartyid;

    private String authMode;

    private String sabpVersion;

    private String flexSource;

    private String flexUbsComp;

    private String flexUserId;

    private String flexUserLoginId;


}
