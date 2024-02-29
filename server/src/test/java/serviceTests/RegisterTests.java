package serviceTests;

import Failures.F403;
import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.RegisterService;

import java.util.Objects;

import static dataAccess.MemoryAuthDAO.authTokens;
import static dataAccess.MemoryGameDAO.games;
import static dataAccess.MemoryUserDAO.users;
import static org.junit.jupiter.api.Assertions.*;
import static service.ClearService.clear;

public class RegisterTests {
    static RegisterService registerService;
    static UserDAO userDAO;
    @BeforeAll
    static void setup() {
        registerService = new RegisterService();
        userDAO = new MemoryUserDAO();
    }

    @BeforeEach
    void resetDAO() throws DataAccessException {
        userDAO.clear();
    }
   @Test
    void registerTestPositive() throws Exception {
        UserData newUser = new UserData("name", "pass", "sillyguy@email.com");

        AuthData authData = registerService.register(newUser);

        assertNotNull(authData);
        assertNotNull(userDAO.getUser("name"));
   }

   @Test
    void registerTestNegative() throws Exception {
        UserData existingUser = new UserData("me", "word", "me@mail.org");
        UserData sameName = new UserData("me", "pass", "lol@mail.com");

        registerService.register(existingUser);

        Exception exception = assertThrows(F403.class, () -> {
            registerService.register(sameName);
        });

        String expectedMessage = "Error: already taken";
        String actualMessage = exception.getMessage();

       assertEquals(actualMessage, expectedMessage);
   }

}
