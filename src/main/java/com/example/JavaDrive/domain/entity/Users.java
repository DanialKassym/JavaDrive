package com.example.JavaDrive.domain.entity;

import com.example.JavaDrive.domain.enums.RoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    private String email;

    @Column(nullable = false)
    private String password_hash;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date created_user_at;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleEnum role;


    public Users(String username, String email, String password_hash, Date created_user_at, RoleEnum role) {
        this.username = username;
        this.email = email;
        this.password_hash = password_hash;
        this.created_user_at = created_user_at;
        this.role = role;
    }
}

