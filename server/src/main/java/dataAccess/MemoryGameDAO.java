package dataAccess;

import model.GameData;
import model.UserData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{

    static final private HashMap<Integer, GameData> games = new HashMap<>();

    public static void clear() throws DataAccessException{
        games.clear();
    }
}
