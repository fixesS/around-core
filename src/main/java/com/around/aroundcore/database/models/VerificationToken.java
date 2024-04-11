package com.around.aroundcore.database.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_tokens")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VerificationToken {

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
