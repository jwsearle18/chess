package service;

import Failures.F400;
import Failures.F403;
import dataAccess.*;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class RegisterService {
    public AuthData register(UserData user) throws DataAccessException, F400, F403 {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();

        //since this is register, don't worry about verifying that the user is authenticated (you will have to do tha tin the login)
        //put the authdata into your hashmap in authDAO
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
