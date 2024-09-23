package com.around.aroundcore.web.exceptions.entity;

import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;

public class GameUserTeamNullForRound extends ApiException {
  public GameUserTeamNullForRound(){
    super(ApiResponse.USER_HAS_NO_TEAM_IN_ROUND);
  }
}
