package service;

import Failures.F400;
import Failures.F401;
import Failures.F403;
import dataAccess.*;
import requests.JoinGameRequest;
import results.JoinGameResult;

public class JoinGameService {
    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) throws DataAccessException, F400, F401, F403 {
        GameDAO gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();

        return null;
    }
}
