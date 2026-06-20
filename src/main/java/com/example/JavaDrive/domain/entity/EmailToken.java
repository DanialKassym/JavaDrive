package com.example.JavaDrive.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "emailtoken")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;
    private String email;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date Expiry;

    @Column(nullable = false)
    private Boolean verified;

    public EmailToken(String token, String email, Date expiry, Boolean verified) {
        this.token = token;
        this.email = email;
        Expiry = expiry;
        this.verified = verified;
    }
}

