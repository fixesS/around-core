package com.around.aroundcore;

import com.around.aroundcore.database.models.GameUser;
import com.around.aroundcore.database.models.Team;
import com.around.aroundcore.web.dtos.UpdateGameUserDTO;
import com.around.aroundcore.web.services.EntityPatcher;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EntityPatcherTest {

    @Autowired
    private EntityPatcher patcher;

    @Test
    void patchFieldsFromDtoToEntity1() throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Team team = new Team();
        GameUser gameUser = new GameUser();
        gameUser.setTeam(team);

        UpdateGameUserDTO userDTO = UpdateGameUserDTO.builder()
                .email("email")
                .city("EKB")
                .username("username")
                .build();

        patcher.patch(gameUser, userDTO);

        Assertions.assertNull(gameUser.getCoins());
        Assertions.assertNull(gameUser.getLevel());
        Assertions.assertEquals(userDTO.getEmail(), gameUser.getEmail());
        Assertions.assertEquals(userDTO.getUsername(), gameUser.getUsername());
        Assertions.assertEquals(userDTO.getCity(), gameUser.getCity());
        log.info(userDTO.toString());
        log.info(gameUser.toString());
    }
    @Test
    void patchFieldsFromDtoToEntity2() throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Team team = new Team();
        GameUser gameUser = new GameUser();
        gameUser.setTeam(team);

        UpdateGameUserDTO userDTO = UpdateGameUserDTO.builder()
                .city("EKB")
                .build();

        patcher.patch(gameUser, userDTO);

        Assertions.assertNull(gameUser.getId());
        Assertions.assertNull(gameUser.getCoins());
        Assertions.assertNull(gameUser.getLevel());
        Assertions.assertEquals(userDTO.getEmail(), gameUser.getEmail());
        Assertions.assertEquals(userDTO.getUsername(), gameUser.getUsername());
        Assertions.assertEquals(userDTO.getCity(), gameUser.getCity());
        log.info(userDTO.toString());
        log.info(gameUser.toString());
    }
    @Test
    void patchFieldsFromDtoToEntity3() throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Team team = new Team();
        GameUser gameUser = new GameUser();
        gameUser.setTeam(team);
        gameUser.setUsername("gggggg");
        gameUser.setEmail("eeeeee");

        UpdateGameUserDTO userDTO = UpdateGameUserDTO.builder()
                .city("EKB")
                .build();

        patcher.patch(gameUser, userDTO);

        Assertions.assertNull(gameUser.getCoins());
        Assertions.assertNull(gameUser.getLevel());
        Assertions.assertEquals(gameUser.getEmail(), gameUser.getEmail());
        Assertions.assertEquals(gameUser.getUsername(), gameUser.getUsername());
        Assertions.assertEquals(userDTO.getCity(), gameUser.getCity());
        log.info(userDTO.toString());
        log.info(gameUser.toString());
    }
    @Test
    void patchFieldsFromDtoToEntity4() throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Team team = new Team();
        GameUser gameUser = new GameUser();
        gameUser.setTeam(team);
        gameUser.setUsername("gggggg");
        gameUser.setEmail("eeeeee");

        UpdateGameUserDTO userDTO = UpdateGameUserDTO.builder()
                .city("EKB")
                .email("email")
                .build();

        patcher.patch(gameUser, userDTO);

        Assertions.assertNull(gameUser.getCoins());
        Assertions.assertNull(gameUser.getLevel());
        Assertions.assertEquals(userDTO.getEmail(), gameUser.getEmail());
        Assertions.assertEquals(gameUser.getUsername(), gameUser.getUsername());
        Assertions.assertEquals(userDTO.getCity(), gameUser.getCity());
        log.info(userDTO.toString());
        log.info(gameUser.toString());
    }
}
