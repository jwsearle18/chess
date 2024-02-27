package service;

import Failures.F401;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import requests.Game;
import requests.ListGamesRequest;
import results.ListGamesResult;

import java.util.ArrayList;
import java.util.HashMap;

public class ListGamesService {

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws DataAccessException, F401 {
        GameDAO gameDAO  = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();

        AuthData authData = authDAO.getAuth(listGamesRequest.authToken());
        if(authData != null){
            ArrayList<Game> gamesList = gameDAO.listGames();
            return new ListGamesResult(gamesList);
        } else {
            throw new F401("Error: unauthorized");
        }
    }
}
