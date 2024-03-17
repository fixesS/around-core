package com.around.aroundcore.database.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sessions", schema = "public")
public class Session {
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


}
