package com.quantum_pixel.arg.controller;

import com.quantum_pixel.arg.ConfigTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = "/db/users.sql",
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

public class UserControllerIT extends ConfigTest {

    private static final String BASE_PATH = "http://localhost:8080/api/v1";


    @Test
    public void getAllUserTest() throws Exception {
        given()
                .get(BASE_PATH + "/users")
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }


    @Test
    public void createUserTest() {
        given()
                .contentType(ContentType.JSON)
                .body(
                        "[{" +
                                "    \"firstName\": \"luka\",\n" +
                                "    \"lastName\":\"buziu\",\n" +
                                "\"email\":\"lukabuziu42@gmail.com\"" +
                                "}]"
                )
                .when()
                .request("POST", BASE_PATH + "/users")
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(List.of(31)));
    }

    @Test

    public void updateUserTest() {
        given()
                .contentType(ContentType.JSON)
                .body(
                        "[{" +
                                " \"id\" : \"1\" ,\n" +
                                "    \"firstName\": \"indrit\",\n" +
                                "    \"lastName\":\"vaka\",\n" +
                                "\"email\":\"lukabuziu42@gmail.com\"" +
                                "}]"
                )
                .when()
                .request("PUT", BASE_PATH + "/users")
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("firstName", equalTo(List.of("indrit")))
                .body("lastName", equalTo(List.of("vaka")));
    }


    @Override
    protected List<String> getTableToTruncate() {
        return List.of("users");
    }
}
