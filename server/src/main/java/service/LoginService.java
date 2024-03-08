package service;

import Failures.F401;
import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import requests.LoginRequest;

public class LoginService {
    public AuthData login(LoginRequest loginRequest) throws DataAccessException, F401 {
        UserDAO userDAO = new SQLUserDAO();
        AuthDAO authDAO = new SQLAuthDAO();
        String reqUsername = loginRequest.username();
        UserData userData = userDAO.getUser(reqUsername);
        if (userData == null) {
            throw new F401("Error: unauthorized");
        } else {
            String storedPassword = userData.password();
            if (verifyUser(loginRequest.password(), storedPassword)) {
                return authDAO.createAuth(loginRequest.username());
            } else {
                throw new F401("Error: unauthorized");
            }
        }
    }

    boolean verifyUser(String providedClearTextPassword, String hashedPassword) {
        // read the previously hashed password from the database

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(providedClearTextPassword, hashedPassword);
    }
}
