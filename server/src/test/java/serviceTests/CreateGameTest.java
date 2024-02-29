package serviceTests;

import Failures.F400;
import dataAccess.*;
import model.AuthData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.CreateGameRequest;
import results.CreateGameResult;
import service.CreateGameService;

import static org.junit.jupiter.api.Assertions.*;

public class CreateGameTest {
    static CreateGameService createGameService;
    static AuthDAO authDAO;
    static GameDAO gameDAO;

    @BeforeAll
    static void setup() {
        createGameService = new CreateGameService();
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
    }

    @BeforeEach
    void resetDAOs() throws DataAccessException {
        gameDAO.clear();
        authDAO.clear();
    }

    @Test
    void createGamePositive() throws Exception {
        String username = "username";
        String gameName = "gameName";
        AuthData authData = authDAO.createAuth(username);
        CreateGameRequest createGameRequest = new CreateGameRequest(authData.authToken(), gameName);

        CreateGameResult createGameResult = CreateGameService.createGame(createGameRequest);

        assertNotNull(createGameResult);
        assertNotNull(gameDAO.getGame(createGameResult.gameID()));
    }

    @Test
    void createGameNegative() throws Exception {
        String username = "username";
        AuthData authData = authDAO.createAuth(username);
        CreateGameRequest createGameRequest = new CreateGameRequest(authData.authToken(), null);

        Exception exception = assertThrows(F400.class, () -> {
            CreateGameService.createGame(createGameRequest);
        });

        String expectedMessage = "Error: bad request";
        assertEquals(expectedMessage, exception.getMessage());
    }
}
