package com.around.aroundcore.database.models.oauth;

import com.around.aroundcore.database.models.user.GameUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OAuthUserEmbedded implements Serializable {
    @Enumerated(EnumType.STRING)
    @Column(name = "oauth_provider")
    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    @Getter
    private OAuthProvider oauthProvider;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private GameUser gameUser;


    @Override
    public int hashCode() {
        return Objects.hash(oauthProvider, gameUser);
    }
}
