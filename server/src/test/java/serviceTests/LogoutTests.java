package serviceTests;

import Failures.F401;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.LogoutRequest;
import service.LogoutService;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutTests {

    static LogoutService logoutService;
    static AuthDAO authDAO;

    @BeforeAll
    static void setup() {
        logoutService = new LogoutService();
        authDAO = new MemoryAuthDAO();
    }

    @BeforeEach
    void resetDAO() throws DataAccessException {
        authDAO.clear();
    }

    @Test
    void logoutTestPositive() throws Exception {

        String username = "username";
        AuthData authData = authDAO.createAuth(username);
        String authToken = authData.authToken();

        LogoutRequest logoutRequest = new LogoutRequest(authToken);

        assertDoesNotThrow(() -> logoutService.logout(logoutRequest));
        assertNull(authDAO.getAuth(authToken));

    }

    @Test
    void logoutTestNegative() {

        String badAuthToken = "1234";
        LogoutRequest logoutRequest = new LogoutRequest(badAuthToken);

        Exception exception = assertThrows(F401.class, () -> {
            logoutService.logout(logoutRequest);
        });

        String expectedMessage = "Error: unauthorized";
        assertEquals(exception.getMessage(), expectedMessage);
    }


}
