package com.quantum_pixel.arg.hotel.controller;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.quantum_pixel.arg.AppITConfig;
import com.quantum_pixel.arg.ConfigTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@AppITConfig
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ConferenceMailSenderControllerIT extends ConfigTest {
    @Autowired
    private ServletWebServerApplicationContext webServerAppContext;

    @RegisterExtension
    static GreenMailExtension greenMailExtension = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("luka", "buziu"))
            .withPerMethodLifecycle(true);

    @Test
    void shouldSentSuccessfully() {
        String payload = """
                {  \s
                    "firstName" : "luka",
                    "lastName" : "Buziu",
                    "email": "lukabuziu22@gmail.com",
                    "conferenceReservations" : [
                        {
                            "reservationDate": "2024-11-16",
                            "startTime": "14:00",
                            "endTime": "16:00"
                        },
                                {
                            "reservationDate": "2024-11-17",
                            "startTime": "12:00",
                            "endTime": "17:00"
                        }
                    ],
                    "emailContent":"Some information about the email"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post(AppITConfig.BASE_URL
                        + webServerAppContext.getWebServer().getPort()
                        + AppITConfig.V1 + "/contact-us/email").andReturn()
                .then()
                .statusCode(HttpStatus.OK.value());

        assertThat(greenMailExtension.getReceivedMessages().length)
                .isEqualTo(1);

    }

    @Test
    void throwsException() {
        String payload =
                """
                        {  \s
                            "firstName" : "luka",
                            "lastName" : "Buziu",
                            "email": "lukabuziu22@gmail.com",
                            "conferenceReservations" : [
                                {
                                    "reservationDate": "2024-03-16",
                                    "startTime": "14:00",
                                    "endTime": "16:00",
                                },
                                        {
                                    "reservationDate": "2024-03-27",
                                    "startTime": "12:00",
                                    "endTime": "11:00",
                                }
                            ],
                            "emailContent":"Some information about the email"
                        }""";

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post(AppITConfig.BASE_URL
                        + webServerAppContext.getWebServer().getPort()
                        + AppITConfig.V1 + "/contact-us/email"
                ).andReturn()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Override
    protected List<String> getTableToTruncate() {
        return null;
    }
}
