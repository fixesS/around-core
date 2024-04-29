package com.around.aroundcore.web.dtos.events.timepad;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TimepadEvents {
    private Integer total;
    private List<TimepadEvent> values;
}
