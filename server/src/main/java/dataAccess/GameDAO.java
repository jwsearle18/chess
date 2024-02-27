package dataAccess;

import model.GameData;
import requests.Game;
import results.CreateGameResult;

import java.util.ArrayList;

public interface GameDAO {

    public void createGame(GameData gameData) throws DataAccessException;
    public void clear() throws DataAccessException;
    public GameData getGame(int gameID) throws DataAccessException;
    public void updateGame(GameData gameData) throws DataAccessException;
    public ArrayList<Game> listGames() throws DataAccessException;
}
