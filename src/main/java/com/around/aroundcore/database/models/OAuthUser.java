package com.around.aroundcore.database.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users_oauth")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthUser {

    @EmbeddedId
    private OAuthUserEmbedded oAuthUserEmbedded;

    @Getter
    @Column(name = "oauth_id")
    private Long oauthId;

    public OAuthProvider getProvider() {
        return this.oAuthUserEmbedded.getOauthProvider();
    }
}
