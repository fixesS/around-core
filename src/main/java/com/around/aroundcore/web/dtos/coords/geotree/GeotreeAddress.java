package com.around.aroundcore.web.dtos.coords.geotree;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GeotreeAddress {
    private String value;
    private GeotreeCoords geo_center;
}
