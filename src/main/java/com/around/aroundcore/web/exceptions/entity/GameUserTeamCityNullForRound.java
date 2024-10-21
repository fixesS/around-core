package com.around.aroundcore.web.exceptions.entity;

import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;

public class GameUserTeamCityNullForRound extends ApiException {
  public GameUserTeamCityNullForRound(){
    super(ApiResponse.USER_HAS_NO_TEAM_IN_ROUND);
  }
}
