package com.quantum_pixel.arg.controller;

import com.quantum_pixel.arg.AppITConfig;
import com.quantum_pixel.arg.ConfigTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@AppITConfig
@Sql(
        scripts = "/db/users.sql",
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)


public class UserControllerIT extends ConfigTest {

    @Autowired
    private ServletWebServerApplicationContext webServerAppContext;


    @Test
    public void getAllUserTest() throws Exception {
        given()
                .get(AppITConfig.BASE_URL
                        + webServerAppContext.getWebServer().getPort()
                        + AppITConfig.V1
                        + "/users")
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
                                "    \"lastName\":\"buziu\"\n," +
                                "\"email\":\"lukabuziu42@gmail.com\"" +
                                "}]"
                )
                .when()
                .request("POST", AppITConfig.BASE_URL
                        + webServerAppContext.getWebServer().getPort()
                        + AppITConfig.V1 + "/users")
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
                                "    \"lastName\":\"vaka\"\n," +
                                "\"email\":\"indrit.vaka@gmail.com\"" +
                                "}]"
                )
                .when()
                .request("PUT", AppITConfig.BASE_URL
                        + webServerAppContext.getWebServer().getPort()
                        + AppITConfig.V1 + "/users")
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
