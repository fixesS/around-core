package com.around.aroundcore.web.dtos.events.timepad;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class TimepadEvent {
    private Integer id;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss+SSSZ")
    private Date created_at;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss+SSSZ")
    private Date starts_at;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss+SSSZ")
    private Date ends_at;
    private String name;
    private String url;
    private TimepadLocation location;
    private List<TimepadCategory> categories;
}
