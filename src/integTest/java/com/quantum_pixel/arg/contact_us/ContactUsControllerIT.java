package com.quantum_pixel.arg.contact_us;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.quantum_pixel.arg.AppITConfig;
import com.quantum_pixel.arg.ConfigTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AppITConfig
public class ContactUsControllerIT extends ConfigTest {

    @Autowired
    private ServletWebServerApplicationContext webServerAppContext;


    @RegisterExtension
    static GreenMailExtension greenMailExtension = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("luka", "buziu"))
            .withPerMethodLifecycle(true);


    @Test
    void mailShouldSentSuccessfully() {
        String payload = """
                {  \s
                    "fullNameOrCompanyName" : "luka",
                    "email": "some03642@gmail.com",
                    "message":"Some information about the email"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post(AppITConfig.BASE_URL
                        + webServerAppContext.getWebServer().getPort()
                        + AppITConfig.V1
                        + "/contact-us/contact-us-message").andReturn()
                .then()
                .statusCode(HttpStatus.OK.value());

        assertThat(greenMailExtension.getReceivedMessages().length)
                .isEqualTo(1);
    }
    @Override
    protected List<String> getTableToTruncate() {
        return null;
    }
}
