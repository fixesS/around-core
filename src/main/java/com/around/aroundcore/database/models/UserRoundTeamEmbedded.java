package com.around.aroundcore.database.models;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serializable;

//@Embeddable
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRoundTeamEmbedded implements Serializable {
    private Round round;
    private GameUser user;

}
