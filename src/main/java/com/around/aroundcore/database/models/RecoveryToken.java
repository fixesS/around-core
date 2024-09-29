package com.around.aroundcore.database.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recovery_tokens")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecoveryToken {
    @Id
    private Integer user_id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id")
    private GameUser user;
    @Column
    private String token;
    @Column(name = "created")
    private LocalDateTime created;
    @Column(name = "expires")
    private LocalDateTime expiresIn;
}
