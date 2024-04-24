package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.models.Skill;
import com.around.aroundcore.database.services.GameUserService;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.web.dtos.*;
import com.around.aroundcore.web.enums.ApiResponse;
import com.around.aroundcore.web.exceptions.api.ApiException;
import com.around.aroundcore.web.exceptions.entity.*;
import com.around.aroundcore.web.mappers.GameUserDTOMapper;
import com.around.aroundcore.web.mappers.SkillDTOMapper;
import com.around.aroundcore.web.services.EntityPatcher;
import io.swagger.v3.oas.annotations.Operation;
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
    private final SkillDTOMapper skillDTOMapper;
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
            List<Skill> skills = user.getUserSkills().stream().map(gameUserSkill -> gameUserSkill.getGameUserSkillEmbedded().getSkill()).toList();
            skillDTOS = skills.stream().map(skillDTOMapper).toList();
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
            if(updateGameUserDTO.getEmail()!=null){
                userService.checkEmail(updateGameUserDTO.getUsername());
            }
            patcher.patch(user,updateGameUserDTO);
            userService.update(user);
            gameUserDTO = gameUserDTOMapper.apply(user);
            response = ApiResponse.OK;
        }  catch (GameUserNullException e) {
            response = ApiResponse.USER_DOES_NOT_EXIST;
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
    @GetMapping("/{username}")
    @Operation(
            summary = "Gives all info about user by username",
            description = "Allows to get all info about user by username."
    )
    public ResponseEntity<GameUserDTO> getUserByIUsername(@PathVariable String username){
        ApiResponse response;
        GameUserDTO gameUserDTO = null;

        try {
            var user = userService.findByUsername(username);
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
    @GetMapping("{username}/friends")
    @Operation(
            summary = "Gives friends of user my username",
            description = "Allows get info about all friends of user."
    )
    public ResponseEntity<List<GameUserDTO>> getUserFriendsByUsername(@PathVariable String username) {
        ApiResponse response;
        List<GameUserDTO> friends = null;

        try {
            var user = userService.findByUsername(username);
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
    @GetMapping("/{username}/followers")
    @Operation(
            summary = "Gives friends",
            description = "Allows get info about all friends of user."
    )
    public ResponseEntity<List<GameUserDTO>> getUserFollowersByUsername(@PathVariable String username) {
        ApiResponse response;
        List<GameUserDTO> followers = null;

        try {
            var user = userService.findByUsername(username);
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
    public ResponseEntity<String> postMeFollowUser(@RequestParam("username") String username){
        ApiResponse response;

        try {
            var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var session = sessionService.findByUuid(sessionUuid);
            var user = session.getUser();
            var userToFollow = userService.findByUsername(username);
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
    public ResponseEntity<String> postMeUnfollowUser(@RequestParam("username") String username){
        ApiResponse response;

        try {
            var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var session = sessionService.findByUuid(sessionUuid);
            var user = session.getUser();

            var userToUnfollow = userService.findByUsername(username);
            user.unfollowUser(userToUnfollow);
            userService.update(user);
            userService.update(userToUnfollow);
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
    @PostMapping("me/removeFriend")
    @Operation(
            summary = "unfollow user",
            description = "Allows to remove user from your friends by it's username. User becomes your follower."
    )
    @Transactional
    public ResponseEntity<String> postMeRemoveFriend(@RequestParam("username") String username){
        ApiResponse response;

        try {
            var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var session = sessionService.findByUuid(sessionUuid);
            var user = session.getUser();

            var userToRemove = userService.findByUsername(username);
            user.removeUserFromFriends(userToRemove);
            userService.update(user);
            userService.update(userToRemove);
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
