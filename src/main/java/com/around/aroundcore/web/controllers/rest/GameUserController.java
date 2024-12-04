package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.user.GameUser;
import com.around.aroundcore.database.models.oauth.OAuthUser;
import com.around.aroundcore.database.models.oauth.OAuthUserEmbedded;
import com.around.aroundcore.database.models.round.Round;
import com.around.aroundcore.database.services.*;
import com.around.aroundcore.web.dtos.events.MapEventDTO;
import com.around.aroundcore.web.dtos.auth.ChangePasswordDTO;
import com.around.aroundcore.web.dtos.auth.OAuthDTO;
import com.around.aroundcore.web.dtos.oauth.OAuthResponse;
import com.around.aroundcore.web.dtos.user.GameUserDTO;
import com.around.aroundcore.web.dtos.SkillDTO;
import com.around.aroundcore.web.dtos.user.UpdateGameUserDTO;
import com.around.aroundcore.core.enums.ApiResponse;
import com.around.aroundcore.core.events.OnEmailVerificationEvent;
import com.around.aroundcore.core.exceptions.api.ApiException;
import com.around.aroundcore.core.exceptions.api.entity.GameUserOAuthProviderAlreadyExistsException;
import com.around.aroundcore.core.exceptions.api.entity.GameUserUsernameNotUnique;
import com.around.aroundcore.web.mappers.GameUserDTOMapper;
import com.around.aroundcore.web.mappers.event.MapEventDTOMapper;
import com.around.aroundcore.web.mappers.SkillDTOWithCurrentLevelMapper;
import com.around.aroundcore.core.services.EntityPatcher;
import com.around.aroundcore.core.services.apis.oauth.OAuthProviderRouter;
import com.around.aroundcore.core.services.apis.oauth.ProviderOAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
    private final PasswordEncoder passwordEncoder;
    private final OAuthProviderRouter oAuthProviderRouter;
    private final GameUserService userService;
    private final GameUserDTOMapper gameUserDTOMapper;
    private final TeamService teamService;
    private final SkillDTOWithCurrentLevelMapper skillDTOMapper;
    private final EntityPatcher patcher;
    private final ApplicationEventPublisher eventPublisher;
    private final RoundService roundService;
    private final CityService cityService;
    private final MapEventDTOMapper mapEventDTOMapper;
    private final GameChunkService gameChunkService;

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
            summary = "Gives friends of user sorted by chunks",
            description = "sorting_by_chunks_all - sorting by chunks all (captured chunks in current round for all cities) if true; sorting ny chunks that owned by users right now if false."
    )
    @Transactional
    public ResponseEntity<List<GameUserDTO>> getMyFriends(@RequestParam("sorting_by_chunks_all") Boolean sortingByChunksAll) {
        var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();
        List<GameUserDTO> friends = getSortedUsersByChunks(
                List.of(roundService.getCurrentRound()),user.getFriends(),50, sortingByChunksAll)
                .stream().map(gameUserDTOMapper).toList();
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
    @GetMapping("/me/events")
    @Operation(
            summary = "Gives visited events by user",
            description = "Allows get info about visited events by user."
    )
    @Transactional
    public ResponseEntity<List<MapEventDTO>> getMyVisitedEvents() {
        var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();
        List<MapEventDTO> mapEventDTOS = user.getVisitedEvents().stream().map(mapEventDTOMapper).toList();

        return ResponseEntity.ok(mapEventDTOS);
    }
    @PatchMapping("/me")
    @Operation(
            summary = "Changes some info about user",
            description = "Allows to change some ingo about user"
    )
    @Transactional
    public ResponseEntity<String> patchMe(@RequestBody @Validated UpdateGameUserDTO updateGameUserDTO){
        var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();
        if(updateGameUserDTO.getUsername()!=null){
            userService.checkUsername(updateGameUserDTO.getUsername());
        }
        patchUser(user,updateGameUserDTO);
        if(updateGameUserDTO.getPassword()!=null){
            updateGameUserDTO.getPassword().checkIsPasswordsDifferent();
            updatePassword(user,updateGameUserDTO.getPassword());
        }
        userService.updateAndFlush(user);
        if(updateGameUserDTO.getTeam_id()!=null && updateGameUserDTO.getCity_id()!=null){
            var team = teamService.findById(updateGameUserDTO.getTeam_id());
            var city = cityService.findById(updateGameUserDTO.getCity_id());
            userService.setTeamForRoundAndCity(user,roundService.getCurrentRound(),city,team);
        }
        return ResponseEntity.ok("");
    }
    @PatchMapping("/me/team")
    @Operation( summary = "Changes user team in current round for city.",
            description = "Team id and city id in request parameters")
    @Transactional
    public ResponseEntity<String> patchMeTeam(@RequestParam("team_id") Integer teamId, @RequestParam("city_id") Integer cityId){
        var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();
        var team = teamService.findById(teamId);
        var city = cityService.findById(cityId);
        if(!user.getTeam(city).equals(team)){
            var round = roundService.getCurrentRound();
            user.reduceCoins(round.getGameSettings().getTeamChangeCost());
            userService.setTeamForRoundAndCity(user,roundService.getCurrentRound(),city,team);
            gameChunkService.deleteChunks(user,round,city);
        }
        return ResponseEntity.ok("");
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
            summary = "Gives friends of user by id sorted by chunks",
            description = "sorting_by_chunks_all - sorting by chunks all (captured chunks in current round for all cities) if true; sorting ny chunks that owned by users right now if false."
    )
    @Transactional
    public ResponseEntity<List<GameUserDTO>> getUserFriendsById(@PathVariable Integer id, @RequestParam("sorting_by_chunks_all") Boolean sortingByChunksAll) {
        var user = userService.findById(id);
        List<GameUserDTO> friends = getSortedUsersByChunks(
                List.of(roundService.getCurrentRound()),user.getFriends(),50, sortingByChunksAll)
                .stream().map(gameUserDTOMapper).toList();
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
            summary = "Gives sorted suggested users by chunks",
            description = "sorting_by_chunks_all - sorting by chunks all (captured chunks in current round for all cities) if true; sorting ny chunks that owned by users right now if false."
    )
    @Transactional
    public ResponseEntity<List<GameUserDTO>> getUserByUsername(@RequestParam("username") String username,@RequestParam("sorting_by_chunks_all") Boolean sortingByChunksAll){
        List<GameUserDTO> gameUserDTOS = new ArrayList<>();

        if(!username.isEmpty()){
            List<GameUser> suggestionUsers = userService.findByUsernameContaining(username);
            gameUserDTOS = getSortedUsersByChunks(
                    List.of(roundService.getCurrentRound()),suggestionUsers,50, sortingByChunksAll)
                    .stream().map(gameUserDTOMapper).toList();
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
    public ResponseEntity<String> deleteMyFollower(@RequestParam("followee_id") @Schema(description = "followee id") Integer followerId){
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
    @PatchMapping("me/oauth2")
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
            ProviderOAuthService providerOAuthService = oAuthProviderRouter.getProviderOAuthService(oAuthDTO.getProvider());
            OAuthResponse oAuthResponse = providerOAuthService.checkToken(oAuthDTO.getAccess_token());
            OAuthUser oAuthUser = OAuthUser.builder()
                    .oauthId(oAuthResponse.getUser_id())
                    .oAuthUserEmbedded(new OAuthUserEmbedded(oAuthDTO.getProvider(), user))
                    .build();
            user.addOAuthToUser(oAuthUser);
            userService.update(user);
        }else{
         throw new GameUserOAuthProviderAlreadyExistsException();
        }
        return ResponseEntity.ok("");
    }
    private void patchUser(GameUser user, UpdateGameUserDTO updateGameUserDTO) {
        try {
            patcher.patch(user, updateGameUserDTO);
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            throw new ApiException(ApiResponse.UNKNOWN_ERROR);
        }
    }
    private void updatePassword(GameUser user, ChangePasswordDTO passwordDTO) {
        if (!passwordEncoder.matches(passwordDTO.getOld_password(), user.getPassword())) {
            throw new ApiException(ApiResponse.AUTH_INCORRECT_PASSWORD);
        }
        user.setPassword(passwordEncoder.encode(passwordDTO.getNew_password()));
    }
    private List<GameUser> getSortedUsersByChunks(List<Round> rounds, List<GameUser> users, Integer limit, Boolean sortingByChunksAll){
        if(users.isEmpty()){
            return users;
        }
        return userService.getTopUsersByChunks(rounds,users,limit,sortingByChunksAll);
    }
}
