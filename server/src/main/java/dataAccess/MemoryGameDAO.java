package dataAccess;

import model.GameData;
import model.UserData;
import results.CreateGameResult;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{

    static final private HashMap<Integer, GameData> games = new HashMap<>();

    public CreateGameResult createGame()
    public void clear() throws DataAccessException{
        games.clear();
    }

}
