package com.example.JavaDrive.Users;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "emailtoken")
public class EmailToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;
    private String email;

    @Column(nullable = false)
    private Boolean verified;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date Expiry;

    public EmailToken() {}

    public EmailToken(String token, String email, Boolean verified, Date expiry) {
        this.token = token;
        this.email = email;
        this.verified = verified;
        Expiry = expiry;
    }

    public EmailToken(Long id, String token, String email, Boolean verified, Date expiry) {
        this.id = id;
        this.token = token;
        this.email = email;
        this.verified = verified;
        Expiry = expiry;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Date getExpiry() {
        return Expiry;
    }
}

