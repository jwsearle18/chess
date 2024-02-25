package service;

import Failures.F401;
import dataAccess.*;
import model.AuthData;
import requests.LoginRequest;

public class LoginService {
    public AuthData login(LoginRequest loginRequest) throws DataAccessException, F401 {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();

        String storedUsername = userDAO.getUser(loginRequest.username()).username();
        String storedPassword = userDAO.getUser(loginRequest.username()).password();
        if(loginRequest.password().equals(storedPassword)) {
            AuthData newAuthToken = authDAO.createAuth(loginRequest.username());
            return newAuthToken;
        } else {
            throw new F401("Error: unauthorized");
        }

    }
}
