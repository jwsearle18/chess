package serviceTests;

import Failures.F401;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.ListGamesRequest;
import results.ListGamesResult;
import service.ListGamesService;

import static org.junit.jupiter.api.Assertions.*;

public class ListGamesTests {
    static ListGamesService listGamesService;
    static GameDAO gameDAO;
    static AuthDAO authDAO;

    @BeforeAll
    static void setup() {
        listGamesService = new ListGamesService();
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
    }

    @BeforeEach
    void resetDAOs() throws DataAccessException {
        gameDAO.clear();
        authDAO.clear();
    }

    @Test
    void listGamesTestPositive() throws Exception {
        String username = "username";
        AuthData authData = authDAO.createAuth(username); // Simulate valid login
        gameDAO.createGame(new GameData(1234, "someGuy", null, "someGame", null));
        gameDAO.createGame(new GameData(987, "anotherGuy", "person", "anotherGame", null));
        ListGamesRequest listGamesRequest = new ListGamesRequest(authData.authToken());

        ListGamesResult listGamesResult = listGamesService.listGames(listGamesRequest);

        assertNotNull(listGamesResult);
        assertEquals(2, listGamesResult.games().size());
    }

    @Test
    void listGamesTestNegative() {
        String badAuthToken = "the wrong guy";
        ListGamesRequest listGamesRequest = new ListGamesRequest(badAuthToken);

        Exception exception = assertThrows(F401.class, () -> {
            listGamesService.listGames(listGamesRequest);
        });

        String expectedMessage = "Error: unauthorized";
        assertEquals(expectedMessage, exception.getMessage());
    }
}
