package com.around.aroundcore.database.models;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRoundTeamEmbedded implements Serializable {
    private Round round;
    private GameUser user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoundTeamEmbedded that = (UserRoundTeamEmbedded) o;
        return Objects.equals(round, that.round) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(round, user);
    }
}
