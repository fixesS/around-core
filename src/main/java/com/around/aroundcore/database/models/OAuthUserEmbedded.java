package com.around.aroundcore.database.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OAuthUserEmbedded {
    @Enumerated(EnumType.STRING)
    @Column(name = "oauth_provider")
    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    @Getter
    private OAuthProvider oauthProvider;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private GameUser gameUser;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OAuthUserEmbedded that = (OAuthUserEmbedded) o;
        return oauthProvider == that.oauthProvider && Objects.equals(gameUser, that.gameUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oauthProvider, gameUser);
    }
}