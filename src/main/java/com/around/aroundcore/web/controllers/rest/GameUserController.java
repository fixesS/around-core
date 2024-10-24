package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.OAuthUser;
import com.around.aroundcore.database.models.OAuthUserEmbedded;
import com.around.aroundcore.database.services.*;
import com.around.aroundcore.web.dtos.auth.OAuthDTO;
import com.around.aroundcore.web.dtos.oauth.OAuthResponse;
import com.around.aroundcore.web.dtos.user.GameUserDTO;
import com.around.aroundcore.web.dtos.SkillDTO;
import com.around.aroundcore.web.dtos.user.UpdateGameUserDTO;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.events.OnEmailVerificationEvent;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.exceptions.entity.*;
import com.around.aroundcore.web.mappers.GameUserDTOMapper;
import com.around.aroundcore.web.mappers.SkillDTOWithCurrentLevelMapper;
import com.around.aroundcore.web.services.EntityPatcher;
import com.around.aroundcore.web.services.apis.oauth.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AroundConfig.API_V1_USER)
@Tag(name="Game User Controller", description="Controller to get info about user")
@SecurityRequirement(name = "JWT")
public class GameUserController {
    private final SessionService sessionService;
    private final OAuthService oAuthService;
    private final GameUserService userService;
    private final GameUserDTOMapper gameUserDTOMapper;
    private final TeamService teamService;
    private final SkillDTOWithCurrentLevelMapper skillDTOMapper;
    private final EntityPatcher patcher;
    private final ApplicationEventPublisher eventPublisher;
    private final RoundService roundService;
    private final CityService cityService;

    @GetMapping("/me")
    @Operation(
            summary = "Gives all info about user for active round",
            description = "Allows to get all info about user."
    )
    @Transactional
    public ResponseEntity<GameUserDTO> getMe(){
        var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();
        GameUserDTO gameUserDTO = gameUserDTOMapper.apply(user);

        return ResponseEntity.ok(gameUserDTO);
    }
  
    @GetMapping("/me/friends")
    @Operation(
            summary = "Gives friends of user",
            description = "Allows get info about all friends of user."
    )
    @Transactional
    public ResponseEntity<List<GameUserDTO>> getMyFriends() {
        var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();
        List<GameUserDTO> friends = getUserFriendsSortedByCurrentRound(user).stream().map(gameUserDTOMapper).toList();

        return ResponseEntity.ok(friends);
    }
    @GetMapping("/me/followers")
    @Operation(
            summary = "Gives followers of user",
            description = "Allows get info about all friends of user."
    )
    @Transactional
    public ResponseEntity<List<GameUserDTO>> getMyFollowers() {
        var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();
        List<GameUserDTO> followers = user.getFollowers().stream().map(gameUserDTOMapper).toList();

        return ResponseEntity.ok(followers);
    }
    @GetMapping("/me/skills")
    @Operation(
            summary = "Gives skills of user",
            description = "Allows get info about all friends of user."
    )
    @Transactional
    public ResponseEntity<List<SkillDTO>> getMySkills() {
        var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();
        List<SkillDTO> skillDTOS = user.getUserSkills().stream().map(skillDTOMapper).toList();

        return ResponseEntity.ok(skillDTOS);
    }
    @PatchMapping("/me")
    @Operation(
            summary = "Changes some ingo about user by id",
            description = "Allows to change some ingo about user by id."
    )
    @Transactional
    public ResponseEntity<GameUserDTO> patchMe(@RequestBody @Validated UpdateGameUserDTO updateGameUserDTO){
        var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();
        if(updateGameUserDTO.getUsername()!=null){
            userService.checkUsername(updateGameUserDTO.getUsername());
        }
        try {
            patcher.patch(user,updateGameUserDTO);
        }  catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            throw new ApiException(ApiResponse.UNKNOWN_ERROR);
        }
        userService.update(user);
        if(updateGameUserDTO.getTeam_id()!=null){
            var team = teamService.findById(updateGameUserDTO.getTeam_id());
            userService.updateTeamForRound(user,team,roundService.getCurrentRound());
        }
        if(updateGameUserDTO.getCity_id()!=null){
            var city = cityService.findById(updateGameUserDTO.getCity_id());
            userService.updateCityForRound(user,city,roundService.getCurrentRound());
        }
        GameUserDTO gameUserDTO = gameUserDTOMapper.apply(user);

        return ResponseEntity.ok(gameUserDTO);
    }
    @GetMapping("/{id}")
    @Operation(
            summary = "Gives all info about user by id in active round",
            description = "Allows to get all info about user by id."
    )
    @Transactional
    public ResponseEntity<GameUserDTO> getUserById(@PathVariable Integer id){
        var user = userService.findById(id);
        GameUserDTO gameUserDTO = gameUserDTOMapper.apply(user);

        return ResponseEntity.ok(gameUserDTO);
    }
    @GetMapping("{id}/friends")
    @Operation(
            summary = "Gives friends of user by id",
            description = "Allows get info about all friends of user by id."
    )
    @Transactional
    public ResponseEntity<List<GameUserDTO>> getUserFriendsById(@PathVariable Integer id) {
        var user = userService.findById(id);
        List<GameUserDTO> friends = user.getFriends().stream().map(gameUserDTOMapper).toList();

        return ResponseEntity.ok(friends);
    }
    @GetMapping("/{id}/followers")
    @Operation(
            summary = "Gives followers of user by id",
            description = "Allows get info about all friends of user by id."
    )
    @Transactional
    public ResponseEntity<List<GameUserDTO>> getUserFollowersById(@PathVariable Integer id) {
        var user = userService.findById(id);
        List<GameUserDTO> followers = user.getFollowers().stream().map(gameUserDTOMapper).toList();

        return ResponseEntity.ok(followers);
    }
    @GetMapping("/{id}/skills")
    @Operation(
            summary = "Gives skills of user",
            description = "Allows get info about all friends of user."
    )
    @Transactional
    public ResponseEntity<List<SkillDTO>> getUserSkillsById(@PathVariable Integer id) {
        var user = userService.findById(id);
        List<SkillDTO> skillDTOS = user.getUserSkills().stream().map(skillDTOMapper).toList();

        return ResponseEntity.ok(skillDTOS);
    }
    @PostMapping("/find")
    @Operation(
            summary = "Gives all info about user by uid",
            description = "Allows to get all info about user by id."
    )
    @Transactional
    public ResponseEntity<List<GameUserDTO>> getUserByUsername(@RequestParam("username") String username){
        List<GameUserDTO> gameUserDTOS = new ArrayList<>();

        if(username != null && !username.isEmpty()){
            List<GameUser> suggestionUsers = userService.findByUsernameContaining(username);
            gameUserDTOS = sortSuggestionUsersByCurrentRound(suggestionUsers).stream().map(gameUserDTOMapper).toList();
        }

        return ResponseEntity.ok(gameUserDTOS);
    }
    @PatchMapping("me/followee")
    @Operation(
            summary = "Follow user",
            description = "Allows to follow user by it's username. If user have already followed you, you will be friends."
    )
    @Transactional
    public ResponseEntity<String> patchMyFollowee(@RequestParam("id") Integer id){
        var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();
        var userToFollow = userService.findById(id);

        try {
            user.followUser(userToFollow);
        }catch (GameUserUsernameNotUnique e) {
            throw new ApiException(ApiResponse.USER_CANNOT_BE_FRIEND_TO_HIMSELF);
        }

        userService.update(user);
        userService.update(userToFollow);

        return ResponseEntity.ok("");
    }
    @DeleteMapping("me/followee")
    @Operation(
            summary = "unfollow user",
            description = "Allows to unfollow user by it's username. Does nothing if user and you are friends. Unfollow you from user, if you was it's follower."
    )
    @Transactional
    public ResponseEntity<String> deleteMyFollower(@RequestParam("follower_id") @Schema(description = "follower id") Integer followerId){
        var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();
        var userToUnfollow = userService.findById(followerId);
        user.unfollowUser(userToUnfollow);
        userService.update(user);
        userService.update(userToUnfollow);

        return ResponseEntity.ok("");
    }
    @DeleteMapping("me/friend")
    @Operation(
            summary = "'unfriend' user",
            description = "Allows to remove user from your friends by it's username. User becomes your follower."
    )
    @Transactional
    public ResponseEntity<String> deleteMyFriend(@RequestParam("friend_id") @Schema(description = "friend id") Integer friendId){

        var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();
        var friendToRemove = userService.findById(friendId);
        user.removeUserFromFriends(friendToRemove);
        userService.update(user);
        userService.update(friendToRemove);

        return ResponseEntity.ok("");
    }
    @PostMapping("me/verify")
    @Operation(
            summary = "Verifying email",
            description = "Allows to verify user email."
    )
    @Transactional
    public ResponseEntity<String> verifyMe(){
        var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();
        eventPublisher.publishEvent(new OnEmailVerificationEvent(user));

        return ResponseEntity.ok("");
    }
    @PatchMapping("me/oauth")
    @Operation(
            summary = "Adding provider account as oauth login ",
            description = "Allows user to add account in Google or Vk, to login with it in future."
    )
    @Transactional
    public ResponseEntity<String> addOAuthAccount(@RequestBody OAuthDTO oAuthDTO){
        var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();
        if(!userService.isOAuthProviderAccountAdded(user, oAuthDTO.getProvider())){
            OAuthResponse oAuthResponse = oAuthService.getOAuthResponse(oAuthDTO.getProvider(),oAuthDTO.getAccess_token());
            OAuthUser oAuthUser = OAuthUser.builder()
                    .oauthId(oAuthResponse.getUser_id())
                    .oAuthUserEmbedded(new OAuthUserEmbedded(oAuthDTO.getProvider(), user))
                    .build();
            user.addOAuthToUser(oAuthUser);
            userService.create(user);
        }else{
         throw new GameUserOAuthProviderAlreadyExistsException();
        }
        return ResponseEntity.ok("");
    }

    private List<GameUser> getUserFriendsSortedByCurrentRound(GameUser user){
        try{
            return user.getFriends().stream().sorted(Comparator.comparingLong(friend -> -friend.getCapturedChunks())).toList();
        }catch (RoundNullException | NoActiveRoundException  e){
            return user.getFriends();
        }
    }
    private List<GameUser> sortSuggestionUsersByCurrentRound(List<GameUser> suggestions){
        try{
            return suggestions.stream().sorted(Comparator.comparingLong(user -> -user.getCapturedChunks())).toList();
        }catch (RoundNullException e ){
            return suggestions;
        }
    }
}
