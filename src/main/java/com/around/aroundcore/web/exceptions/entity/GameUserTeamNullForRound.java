package com.around.aroundcore.web.exceptions.entity;

public class GameUserTeamNullForRound extends RuntimeException {
  public GameUserTeamNullForRound(){
    super("GameUser team is null for this round");
  }

  public GameUserTeamNullForRound(String message){
    super(message);
  }
}
