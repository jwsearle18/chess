package clientTests;

import org.junit.jupiter.api.*;
import server.Server;
import ui.HttpClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerFacadeTests {

    private static Server server;
    private static HttpClient httpClient;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        httpClient = new HttpClient(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    void joinGamePositive() {
    }

    @Test
    void joinGameNegative() {
    }

    @Test
    void listGamesPositive() {
    }
    @Test
    void listGamesNegative() {
    }

    @Test
    void postCreateGamePositive() {
    }
    @Test
    void postCreateGameNegative() {
    }

    @Test
    void postLogoutPositive() {
    }
    @Test
    void postLogoutNegative() {
    }

    @Test
    void postLoginPositive() {
    }
    @Test
    void postLoginNegative() {
    }

    @Test
    void postRegisterPositive() {
        String username = "jaden" + System.currentTimeMillis();
        String password = "jadenPass";
        String email = "jaden" + System.currentTimeMillis() + "@jaden.com";

        String response = httpClient.postRegister(username, password, email);
        assertNotNull(response);
        assertTrue(response.contains("Registration successful"));
    }
    @Test
    void postRegisterNegative() {
        String username = "sameGuy";
        String password = "pass";
        String email = "sameGuy@jaden.com";

        httpClient.postRegister(username, password, email); // First attempt
        String response = httpClient.postRegister(username, password, email); // Second attempt should fail
        assertTrue(response.contains("403"));
    }

}
