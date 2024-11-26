package com.around.aroundcore.web.dtos.round;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CreateRoundDTO {
    @NotBlank(message = "-9003")
    @Size(min = 2,max = 30,message = "-9005")
    private String name;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime starts;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime ends;
    @Min(value=1,message = "-9004")
    private Integer previous_round_id;
    @Nullable
    @Min(value=1,message = "-9006")
    private Integer settings_id;
}
