package com.around.aroundcore.core.exceptions.api;

import com.around.aroundcore.core.enums.ApiResponse;

public class LevelsLessOrEqualZeroException extends ApiException{
    public LevelsLessOrEqualZeroException(){
        super(ApiResponse.LEVELS_MUST_BE_MORE_THAN_ZERO);
    }
}
