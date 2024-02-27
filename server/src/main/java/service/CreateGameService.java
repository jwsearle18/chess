package service;

import Failures.F400;
import Failures.F401;
import dataAccess.*;
import model.AuthData;
import requests.GameName;
import results.CreateGameResult;

public class CreateGameService {

    public CreateGameResult createGame(String authToken, GameName gameName) throws DataAccessException, F400, F401{
        GameDAO gameDAO  = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();

        AuthData authData = authDAO.getAuth(authToken);
        if(authData == null) {
            throw new F401("Error: unauthorized");
        } else if(gameName.gameName() == null) {
            throw new F400("Error: bad request");
        } else {
            
        }
        return null;
    }
}
