package serviceTests;

import Failures.F401;
import dataAccess.DataAccessException;
import dataAccess.MemoryUserDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.LoginRequest;
import service.LoginService;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTests {
    static LoginService loginService;
    static UserDAO userDAO;

    @BeforeAll
    static void setup() {
        loginService = new LoginService();
        userDAO = new MemoryUserDAO();
    }

    @BeforeEach
    void resetDAO() throws DataAccessException {
        userDAO.clear();
    }

    @Test
    void loginTestPositive() throws Exception {
        UserData user = new UserData("you", "pass", "email@email.email");
        userDAO.createUser(user);
        LoginRequest loginRequest = new LoginRequest("you", "pass");

        AuthData authData = loginService.login((loginRequest));

        assertNotNull(authData);

    }

    @Test
    void loginTestNegative() throws Exception{
        UserData user = new UserData("you", "pass", "email@email.email");
        userDAO.createUser(user);
        LoginRequest badLoginRequest = new LoginRequest("you", "word");

        Exception exception = assertThrows(F401.class, () -> {
            loginService.login(badLoginRequest);
        });

        String expectedMessage = "Error: unauthorized";
        assertEquals(exception.getMessage(), expectedMessage);
    }
}

