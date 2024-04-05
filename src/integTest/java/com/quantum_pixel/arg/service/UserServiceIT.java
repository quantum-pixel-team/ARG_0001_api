package com.quantum_pixel.arg.service;


import com.quantum_pixel.arg.AppITConfig;
import com.quantum_pixel.arg.ConfigTest;
import com.quantum_pixel.arg.user.service.UserService;
import com.quantum_pixel.arg.v1.web.model.CreateUserDTO;
import com.quantum_pixel.arg.v1.web.model.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.INFERRED;


@AppITConfig
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = "/db/users.sql",
        config = @SqlConfig(transactionMode = INFERRED),
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)

public class UserServiceIT extends ConfigTest {

    @Autowired
    private UserService service;


    @Override
    protected List<String> getTableToTruncate() {
        return List.of("users");
    }


    @Test
    public void getAllUsersTest() {
        List<UserDTO> allUsers = service.getAllUsers();
        System.out.println(allUsers.size());
        Assertions.assertEquals(30, allUsers.size());
    }

    @Test
    public void createOrUpdateTest() {
        var userDto = CreateUserDTO.builder()
                .firstName("luka")
                .lastName("buziu")
                .email("lukabuziu42@gmail.com").build();
        var users = List.of(userDto);
        service.createUsers(users);
        var allUsers = service.getAllUsers();
        assertThat(allUsers)
                .hasSize(31);
    }

}
