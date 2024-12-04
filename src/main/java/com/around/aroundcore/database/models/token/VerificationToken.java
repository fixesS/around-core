package com.around.aroundcore.database.models.token;

import com.around.aroundcore.database.models.user.GameUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_tokens", schema = "public")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
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
