package com.around.aroundcore.core.exceptions.api.entity;

import com.around.aroundcore.core.enums.ApiResponse;
import com.around.aroundcore.core.exceptions.api.ApiException;

public class GameUserTeamNullForRoundAndAneCity extends ApiException {
  public GameUserTeamNullForRoundAndAneCity(){
    super(ApiResponse.USER_HAS_NO_TEAM_IN_ROUND);
  }
}
