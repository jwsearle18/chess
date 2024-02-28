package serviceTests;



import chess.ChessGame;
import dataAccess.*;
import jdk.jshell.spi.ExecutionControl;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static dataAccess.MemoryAuthDAO.authTokens;
import static dataAccess.MemoryGameDAO.games;
import static dataAccess.MemoryUserDAO.users;
import static org.junit.jupiter.api.Assertions.*;
import static service.ClearService.clear;

public class serviceTests {

    @BeforeAll
    static void fillDAOs() throws DataAccessException {
        GameDAO gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();

        GameData gameData = new GameData(1, "wguy", "bguy", "gameName", new ChessGame());
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
