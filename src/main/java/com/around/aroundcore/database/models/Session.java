package com.around.aroundcore.database.models;

import com.around.aroundcore.web.exceptions.entity.GameUserNullException;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sessions", schema = "public")
public class Session implements Serializable {
    @Id
    @Column(columnDefinition = "BINARY(16)", name = "uuid")
    private UUID sessionUuid;
    @OneToOne
    @JoinColumn(name = "user_id")
    private GameUser user;
    @Column(name = "useragent")
    private String userAgent;
    @Column
    private InetAddress ip;
    @Column(name = "created")
    private LocalDateTime created;
    @Column(name = "refreshed")
    private LocalDateTime lastRefresh;
    @Column(name = "expires")
    private LocalDateTime expiresIn;

    public GameUser getUser() throws GameUserNullException {
        if(user == null){
            throw new GameUserNullException();
        }
        return user;
    }
}
