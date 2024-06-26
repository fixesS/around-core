package com.around.aroundcore.web.dtos.events.timepad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TimepadCategory {
    private Integer id;
    private String name;
}
