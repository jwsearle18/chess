package service;

import Failures.F401;
import dataAccess.*;
import model.AuthData;
import model.UserData;
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
            if (loginRequest.password().equals(storedPassword)) {
                return authDAO.createAuth(loginRequest.username());
            } else {
                throw new F401("Error: unauthorized");
            }
        }
    }
}
