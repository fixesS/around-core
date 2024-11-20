package com.around.aroundcore.web.exceptions.api.entity;

import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;

public class GameUserTeamNullForRoundAndAneCity extends ApiException {
  public GameUserTeamNullForRoundAndAneCity(){
    super(ApiResponse.USER_HAS_NO_TEAM_IN_ROUND);
  }
}
