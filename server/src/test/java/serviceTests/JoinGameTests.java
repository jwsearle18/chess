package serviceTests;

import Failures.F403;
import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.JoinGameRequest;
import service.JoinGameService;

import static org.junit.jupiter.api.Assertions.*;

public class JoinGameTests {
    static JoinGameService joinGameService;
    static GameDAO gameDAO;
    static AuthDAO authDAO;

    @BeforeAll
    static void setup() {
        joinGameService = new JoinGameService();
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
    }

    @BeforeEach
    void resetDAOs() throws DataAccessException {
        gameDAO.clear();
        authDAO.clear();
    }

    @Test
    void joinGameTestPositive() throws Exception {
        String username = "validUser";
        AuthData authData = authDAO.createAuth(username);
        GameData gameData = new GameData(18, null, null, "ChessGame", new ChessGame());
        gameDAO.createGame(gameData);
        JoinGameRequest joinGameRequest = new JoinGameRequest(authData.authToken(), ChessGame.TeamColor.WHITE, 18);

        assertDoesNotThrow(() -> joinGameService.joinGame(joinGameRequest));

        GameData updatedGameData = gameDAO.getGame(18);
        assertEquals(username, updatedGameData.whiteUsername());
    }

    @Test
    void joinGameTestNegative() throws Exception {

        String whiteUsername = "w";
        authDAO.createAuth(whiteUsername);
        GameData gameData = new GameData(18, whiteUsername, null, "someName", new ChessGame());
        gameDAO.createGame(gameData);

        String blackUsername = "b";
        AuthData blackAuthData = authDAO.createAuth(blackUsername);
        JoinGameRequest joinGameRequest = new JoinGameRequest(blackAuthData.authToken(), ChessGame.TeamColor.WHITE, 18);

        Exception exception = assertThrows(F403.class, () -> {
            joinGameService.joinGame(joinGameRequest);
        });

        String expectedMessage = "Error: already taken";
        assertEquals(expectedMessage, exception.getMessage());
    }
}
