package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.services.GameUserService;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.database.services.TeamService;
import com.around.aroundcore.web.dtos.GameUserDTO;
import com.around.aroundcore.web.dtos.SkillDTO;
import com.around.aroundcore.web.dtos.UpdateGameUserDTO;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.exceptions.entity.*;
import com.around.aroundcore.web.mappers.GameUserDTOMapper;
import com.around.aroundcore.web.mappers.SkillDTOWithCurrentLevelMapper;
import com.around.aroundcore.web.services.EntityPatcher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
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
    private final GameUserService userService;
    private final GameUserDTOMapper gameUserDTOMapper;
    private final TeamService teamService;
    private final SkillDTOWithCurrentLevelMapper skillDTOMapper;
    private final EntityPatcher patcher;

    @GetMapping("/me")
    @Operation(
            summary = "Gives all info about user",
            description = "Allows to get all info about user."
    )
    public ResponseEntity<GameUserDTO> getMe(){
        ApiResponse response;
        GameUserDTO gameUserDTO = null;

        try {
            var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var session = sessionService.findByUuid(sessionUuid);
            var user = session.getUser();
            gameUserDTO = gameUserDTOMapper.apply(user);
            response = ApiResponse.OK;
        } catch (SessionNullException e) {
            response = ApiResponse.SESSION_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (GameUserNullException e) {
            response = ApiResponse.USER_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }

        switch (response) {
            case OK -> {
                return new ResponseEntity<>(gameUserDTO,response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
  
    @GetMapping("/me/friends")
    @Operation(
            summary = "Gives friends",
            description = "Allows get info about all friends of user."
    )
    public ResponseEntity<List<GameUserDTO>> getMyFriends() {
        ApiResponse response;
        List<GameUserDTO> friends = null;

        try {
            var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var session = sessionService.findByUuid(sessionUuid);
            var user = session.getUser();
            friends = user.getFriends().stream().map(gameUserDTOMapper).toList();
            response = ApiResponse.OK;
        } catch (SessionNullException e) {
            response = ApiResponse.SESSION_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (GameUserNullException e) {
            response = ApiResponse.USER_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }

        switch (response) {
            case OK -> {
                return new ResponseEntity<>(friends,response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
    @GetMapping("/me/followers")
    @Operation(
            summary = "Gives friends",
            description = "Allows get info about all friends of user."
    )
    public ResponseEntity<List<GameUserDTO>> getMyFollowers() {
        ApiResponse response;
        List<GameUserDTO> followers = null;

        try {
            var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var session = sessionService.findByUuid(sessionUuid);
            var user = session.getUser();
            followers = user.getFollowers().stream().map(gameUserDTOMapper).toList();
            response = ApiResponse.OK;
        } catch (SessionNullException e) {
            response = ApiResponse.SESSION_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (GameUserNullException e) {
            response = ApiResponse.USER_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }

        switch (response) {
            case OK -> {
                return new ResponseEntity<>(followers,response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
    @GetMapping("/me/skills")
    @Operation(
            summary = "Gives friends",
            description = "Allows get info about all friends of user."
    )
    public ResponseEntity<List<SkillDTO>> getMySkills() {
        ApiResponse response;
        List<SkillDTO> skillDTOS = null;

        try {
            var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var session = sessionService.findByUuid(sessionUuid);
            var user = session.getUser();
            skillDTOS = user.getUserSkills().stream().map(skillDTOMapper).toList();
            response = ApiResponse.OK;
        } catch (SessionNullException e) {
            response = ApiResponse.SESSION_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (GameUserNullException e) {
            response = ApiResponse.USER_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (SkillNullException e){
            response = ApiResponse.SKILL_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }

        switch (response) {
            case OK -> {
                return new ResponseEntity<>(skillDTOS,response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
    @PatchMapping("/me")
    @Operation(
            summary = "Changes some ingo about user by id",
            description = "Allows to change some ingo about user by id."
    )
    @Transactional
    public ResponseEntity<GameUserDTO> patchMe(@RequestBody @Validated UpdateGameUserDTO updateGameUserDTO){
        ApiResponse response;
        GameUserDTO gameUserDTO = null;

        try {
            var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var session = sessionService.findByUuid(sessionUuid);
            var user = session.getUser();
            if(updateGameUserDTO.getUsername()!=null){
                userService.checkUsername(updateGameUserDTO.getUsername());
            }
            if(updateGameUserDTO.getTeam_id()!=null){
                var team = teamService.findById(updateGameUserDTO.getTeam_id());
                user.setTeam(team);
            }
//            if(updateGameUserDTO.getEmail()!=null){
//                userService.checkEmail(updateGameUserDTO.getUsername());
//            }
            patcher.patch(user,updateGameUserDTO);
            userService.update(user);
            gameUserDTO = gameUserDTOMapper.apply(user);
            response = ApiResponse.OK;
        } catch (GameUserNullException e) {
            response = ApiResponse.USER_DOES_NOT_EXIST;
            log.error(e.getMessage());
        } catch (TeamNullException e) {
            response = ApiResponse.TEAM_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }catch (GameUserUsernameNotUnique e) {
            response = ApiResponse.USER_NOT_UNIQUE_USERNAME;
            log.error(e.getMessage());
        }catch (GameUserEmailNotUnique e) {
            response = ApiResponse.USER_NOT_UNIQUE_EMAIL;
            log.error(e.getMessage());
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            response = ApiResponse.UNKNOWN_ERROR;
            log.error(e.getMessage());
        }

        switch (response) {
            case OK -> {
                return new ResponseEntity<>(gameUserDTO,response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
    @GetMapping("/{id}")
    @Operation(
            summary = "Gives all info about user by uid",
            description = "Allows to get all info about user by id."
    )
    public ResponseEntity<GameUserDTO> getUserByIUsername(@PathVariable Integer id){
        ApiResponse response;
        GameUserDTO gameUserDTO = null;

        try {
            var user = userService.findById(id);
            gameUserDTO = gameUserDTOMapper.apply(user);
            response = ApiResponse.OK;
        }  catch (GameUserNullException e) {
            response = ApiResponse.USER_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }

        switch (response) {
            case OK -> {
                return new ResponseEntity<>(gameUserDTO,response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
    @GetMapping("{id}/friends")
    @Operation(
            summary = "Gives friends of user by id",
            description = "Allows get info about all friends of user by id."
    )
    public ResponseEntity<List<GameUserDTO>> getUserFriendsById(@PathVariable Integer id) {
        ApiResponse response;
        List<GameUserDTO> friends = null;

        try {
            var user = userService.findById(id);
            friends = user.getFriends().stream().map(gameUserDTOMapper).toList();
            response = ApiResponse.OK;
        } catch (GameUserNullException e) {
            response = ApiResponse.USER_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }

        switch (response) {
            case OK -> {
                return new ResponseEntity<>(friends,response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
    @GetMapping("/{id}/followers")
    @Operation(
            summary = "Gives friends of user by id",
            description = "Allows get info about all friends of user by id."
    )
    public ResponseEntity<List<GameUserDTO>> getUserFollowersById(@PathVariable Integer id) {
        ApiResponse response;
        List<GameUserDTO> followers = null;

        try {
            var user = userService.findById(id);
            followers = user.getFollowers().stream().map(gameUserDTOMapper).toList();
            response = ApiResponse.OK;
        } catch (GameUserNullException e) {
            response = ApiResponse.USER_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }

        switch (response) {
            case OK -> {
                return new ResponseEntity<>(followers,response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
    @PostMapping("me/followUser")
    @Operation(
            summary = "follow user",
            description = "Allows to follow user by it's username. If user have already followed you, you will be friends."
    )
    @Transactional
    public ResponseEntity<String> postMeFollowUser(@RequestParam("id") Integer id){
        ApiResponse response;

        try {
            var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var session = sessionService.findByUuid(sessionUuid);
            var user = session.getUser();
            var userToFollow = userService.findById(id);
            user.followUser(userToFollow);
            userService.update(user);
            userService.update(userToFollow);
            response = ApiResponse.OK;
        }catch (GameUserNullException e) {
            response = ApiResponse.USER_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }catch (GameUserAlreadyFollowed e){
            response = ApiResponse.USER_FOLLOW_ALREADY_EXIST;
            log.error(e.getMessage());
        }catch (GameUserUsernameNotUnique e) {
            response = ApiResponse.USER_CANNOT_BE_FRIEND_TO_HIMSELF;
            log.error(e.getMessage());
        }
        switch (response) {
            case OK -> {
                return new ResponseEntity<>("",response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
    @PostMapping("me/unfollowUser")
    @Operation(
            summary = "unfollow user",
            description = "Allows to unfollow user by it's username. Does nothing if user and you are friends. Unfollow you from user, if you was it's follower."
    )
    @Transactional
    public ResponseEntity<String> postMeUnfollowUser(@RequestParam("follower_id") @Schema(description = "follower id") Integer followerId){
        ApiResponse response;

        try {
            var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var session = sessionService.findByUuid(sessionUuid);
            var user = session.getUser();

            var userToUnfollow = userService.findById(followerId);
            user.unfollowUser(userToUnfollow);
            userService.update(user);
            userService.update(userToUnfollow);
            response = ApiResponse.OK;
        } catch (GameUserNullException e) {
            response = ApiResponse.USER_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }
        switch (response) {
            case OK -> {
                return new ResponseEntity<>("",response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
    @PostMapping("me/removeFriend")
    @Operation(
            summary = "unfollow user",
            description = "Allows to remove user from your friends by it's username. User becomes your follower."
    )
    @Transactional
    public ResponseEntity<String> postMeRemoveFriend(@RequestParam("friend_id") @Schema(description = "friend id") Integer friendId){
        ApiResponse response;

        try {
            var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var session = sessionService.findByUuid(sessionUuid);
            var user = session.getUser();

            var friendToRemove = userService.findById(friendId);
            user.removeUserFromFriends(friendToRemove);
            userService.update(user);
            userService.update(friendToRemove);
            response = ApiResponse.OK;
        }  catch (GameUserNullException e) {
            response = ApiResponse.USER_DOES_NOT_EXIST;
            log.error(e.getMessage());
        }
        switch (response) {
            case OK -> {
                return new ResponseEntity<>("",response.getStatus());
            }
            default -> throw new ApiException(response);
        }
    }
}
