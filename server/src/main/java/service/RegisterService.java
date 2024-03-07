package service;

import Failures.F400;
import Failures.F403;
import dataAccess.*;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class RegisterService {
    public AuthData register(UserData user) throws DataAccessException, F400, F403 {
        UserDAO userDAO = new SQLUserDAO();
        AuthDAO authDAO = new SQLAuthDAO();

        if(user.username() == null || user.password() == null || user.email() == null) {

            throw new F400("Error: bad request");
            // 400 bad request
        }
        else if(userDAO.getUser(user.username()) != null) {
            throw new F403("Error: already taken");
            // 403 already taken
        } else {
            userDAO.createUser(user);
            return authDAO.createAuth(user.username());
        }

        //return AuthData object
    }
}
