package com.around.aroundcore.web.exceptions.api;

import com.around.aroundcore.web.enums.ApiResponse;

public class LevelsLessOrEqualZero extends ApiException{
    public LevelsLessOrEqualZero(){
        super(ApiResponse.LEVELS_MUST_BE_MORE_THAN_ZERO);
    }
}
