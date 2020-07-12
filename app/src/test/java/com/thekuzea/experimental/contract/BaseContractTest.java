package com.thekuzea.experimental.contract;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.thekuzea.experimental.ExperimentalApplication;
import com.thekuzea.experimental.config.mock.PublicationServiceMockConfig;
import com.thekuzea.experimental.config.mock.RoleServiceMockConfig;
import com.thekuzea.experimental.config.mock.SecurityMockConfig;
import com.thekuzea.experimental.config.mock.UserServiceMockConfig;

import static io.restassured.RestAssured.DEFAULT_URI;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ExperimentalApplication.class
)
@ActiveProfiles("contract")
@Import(value = {
        SecurityMockConfig.class,
        UserServiceMockConfig.class,
        RoleServiceMockConfig.class,
        PublicationServiceMockConfig.class
})
public abstract class BaseContractTest {

    @LocalServerPort
    private int port;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Before
    public void setUp() {
        RestAssured.baseURI = String.format("%s:%s%s", DEFAULT_URI, port, contextPath);
    }
}
