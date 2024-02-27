package service;

import Failures.F401;
import dataAccess.*;
import model.AuthData;
import model.UserData;
import requests.LoginRequest;

public class LoginService {
    public AuthData login(LoginRequest loginRequest) throws DataAccessException, F401 {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
//separate into separate calls because getUser is calling for a null value, which doesn't work, so throw exception unauthprized
        String reqUsername = loginRequest.username();
        if (reqUsername == null) {
            throw new F401("Error: unauthorized");
        } else {
            UserData userData = userDAO.getUser(reqUsername);
            String storedPassword = userData.password();
            if (loginRequest.password().equals(storedPassword)) {
                return authDAO.createAuth(loginRequest.username());
            } else {
                throw new F401("Error: unauthorized");
            }
        }
    }
}
