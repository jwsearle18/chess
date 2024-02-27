package service;

import Failures.F401;
import dataAccess.*;
import requests.ListGamesRequest;
import results.ListGamesResult;

public class ListGamesService {

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws DataAccessException, F401 {
        GameDAO gameDAO  = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();

        return null;
    }
}
