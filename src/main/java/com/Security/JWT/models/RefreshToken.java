package com.Security.JWT.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

import static jakarta.persistence.GenerationType.AUTO;
@Data
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(
            strategy = AUTO
    )
    private long id;
    @OneToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    private User user;
    @Column(
            nullable = false,
            unique = true
    )
    private String token;
    @Column(
            nullable = false
    )
    private Instant expiryDate;
}
