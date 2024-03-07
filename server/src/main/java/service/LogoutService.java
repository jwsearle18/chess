package service;

import Failures.F401;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.SQLAuthDAO;
import model.AuthData;
import requests.LogoutRequest;

public class LogoutService {

    public void logout(LogoutRequest logoutRequest) throws DataAccessException, F401 {

        AuthDAO authDAO = new SQLAuthDAO();

        AuthData authData = authDAO.getAuth(logoutRequest.authToken());
        if(authData != null){
            authDAO.deleteAuth(logoutRequest.authToken());
        } else {
            throw new F401("Error: unauthorized");
        }
    }
}
