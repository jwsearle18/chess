package clientTests;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
    void postLogoutPositive() {
        String username = "testLogout" + System.currentTimeMillis();
        String password = "testPassword";
        String email = "testLogout" + System.currentTimeMillis() + "@example.com";

        httpClient.postRegister(username, password, email);
        String loginResponse = httpClient.postLogin(username, password);
        assertNotNull(loginResponse);
        assertTrue(loginResponse.contains("Login successful!"));

        // Logout
        String logoutResponse = httpClient.postLogout();
        assertTrue(logoutResponse.contains("Logout successful!"));
    }

    @Test
    void postLogoutNegative() {
        String logoutResponse = httpClient.postLogout();

        assertTrue(logoutResponse.contains("Logout failed. No active session."));
    }

    @Test
    void postLoginPositive() {
        String username = "jdog" + System.currentTimeMillis();
        String password = "pass";
        String email = username + "@gmail.com";
        httpClient.postRegister(username, password, email);

        String loginResponse = httpClient.postLogin(username, password);
        assertTrue(loginResponse.contains("Login successful!"));
    }
    @Test
    void postLoginNegative() {
        String randomUsername = "nobody" + System.currentTimeMillis();
        String password = "wrongPassword";
        String loginResponse = httpClient.postLogin(randomUsername, password);

        assertTrue(loginResponse.contains("401"));
    }

    @Test
    void postRegisterPositive() {
        String username = "jaden" + System.currentTimeMillis();
        String password = "jadenPass";
        String email = "jaden" + System.currentTimeMillis() + "@jaden.com";

        String response = httpClient.postRegister(username, password, email);
        assertNotNull(response);
        assertTrue(response.contains("Registration successful!"));
    }
    @Test
    void postRegisterNegative() {
        String username = "sameGuy";
        String password = "pass";
        String email = "sameGuy@jaden.com";

        httpClient.postRegister(username, password, email);
        String response = httpClient.postRegister(username, password, email);
        assertTrue(response.contains("403"));
    }

    @Test
    void postCreateGamePositive() {
        String username = "createGameUser" + System.currentTimeMillis();
        String password = "password";
        String email = username + "@test.com";
        httpClient.postRegister(username, password, email);
        httpClient.postLogin(username, password);

        String gameName = "Test Game " + System.currentTimeMillis();
        String createGameResponse = httpClient.postCreateGame(gameName);
        assertTrue(createGameResponse.contains("Game created successfully"));
    }
    @Test
    void postCreateGameNegative() {

    }
    @Test
    void joinGamesPositive() {

    }


    @Test
    void joinGamesNegative() {
    }

    @Test
    void listGamesPositive() {
    }
    @Test
    void listGamesNegative() {
    }


}
