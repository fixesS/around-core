package com.around.aroundcore.web.mappers;

import com.around.aroundcore.database.models.round.Round;
import com.around.aroundcore.web.dtos.RoundDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.function.Function;

@Service
public class RoundDTOMapper implements Function<Round, RoundDTO> {
    @Value("${around.time.locale}")
    private String timeLocale;

    @Override
    public RoundDTO apply(Round round) {
        return RoundDTO.builder()
                .id(round.getId())
                .name(round.getName())
                .starts(timestampToEpoch(round.getStarts()))
                .ends(timestampToEpoch(round.getEnds()))
                .active(round.getActive())
                .build();
    }
    private Long timestampToEpoch(LocalDateTime timestamp) {
        ZoneId zone = ZoneId.of(timeLocale);
        return timestamp.atZone(zone).toEpochSecond();
    }
}
