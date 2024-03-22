package clientTests;

import org.junit.jupiter.api.*;
import server.Server;
import ui.HttpClient;


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
    }
    @Test
    void postRegisterNegative() {
    }

}
