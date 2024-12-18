package com.around.aroundcore.core.services.apis.coords;

import com.around.aroundcore.web.dtos.coords.Location;
import com.around.aroundcore.core.enums.CoordsAPIType;
import com.around.aroundcore.core.exceptions.api.CoordsNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class CoordsAPI {
    CoordsAPIType type;
    public Double[] getCoordsForLocation(Location location) throws CoordsNotFoundException {
        return new Double[0];
    }

    public CoordsAPIType getType() {
        return type;
    }
}
