package serviceTests;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import passoffTests.testClasses.TestModels;
import server.Server;

import java.util.HashMap;

import static dataAccess.MemoryAuthDAO.authTokens;
import static dataAccess.MemoryGameDAO.games;
import static dataAccess.MemoryUserDAO.users;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static service.ClearService.clear;
public class ClearTest {


    @BeforeAll
    static void fillDAOs() throws DataAccessException {
        GameDAO gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();

        GameData gameData = new GameData(1, "w", "b", "gameName", new ChessGame());
        UserData userData = new UserData("jaden", "password", "email");

        gameDAO.createGame(gameData);
        authDAO.createAuth("jaden");
        userDAO.createUser(userData);

    }

    @Test
    void clearTest() throws Exception {
        clear();
        assertEquals(new HashMap<>(), games);
        assertEquals(new HashMap<>(), users);
        assertEquals(new HashMap<>(), authTokens);
    }

}
