package ru.netology.javaspringboottask;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class ProfileIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final GenericContainer<?> devapp =
            new GenericContainer<>(DockerImageName.parse("devapp:latest"))
                    .withExposedPorts(8080)
                    .waitingFor(Wait.forHttp("/profile").forStatusCode(200))
                    .withStartupTimeout(Duration.ofMinutes(2));

    private static final GenericContainer<?> prodapp =
            new GenericContainer<>(DockerImageName.parse("prodapp:latest"))
                    .withExposedPorts(8081)
                    .waitingFor(Wait.forHttp("/profile").forStatusCode(200))
                    .withStartupTimeout(Duration.ofMinutes(2));

    @BeforeAll
    static void setUp() {
        devapp.start();
        prodapp.start();
    }

    @Test
    void devProfileReturnsExpectedMessage() {
        String url = "http://" + devapp.getHost()
                + ":" + devapp.getMappedPort(8080)
                + "/profile";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Current profile is dev", response.getBody());
    }

    @Test
    void productionProfileReturnsExpectedMessage() {
        String url = "http://" + prodapp.getHost()
                + ":" + prodapp.getMappedPort(8081)
                + "/profile";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Current profile is production", response.getBody());
    }
}
