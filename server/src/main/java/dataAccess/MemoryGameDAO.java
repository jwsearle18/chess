package dataAccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;
import requests.Game;
import results.CreateGameResult;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{

    static final private HashMap<Integer, GameData> games = new HashMap<>();

    public void createGame(GameData gameData) {
        games.put(gameData.gameID(), gameData);
    }

    public GameData getGame(int gameID){
        return games.getOrDefault(gameID, null);
    }

    public void updateGame(GameData newGameData) {
        GameData currentGameData = games.get(newGameData.gameID());
        if(!newGameData.equals(currentGameData)){
            games.put(newGameData.gameID(), newGameData);
        }
    }

    public ArrayList<Game> listGames() {

        ArrayList<Game> gameList = new ArrayList<>();
        games.forEach(((integer, gameData) -> {
            Game game = new Game(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName());
            gameList.add(game);
        }));
        return gameList;
    }
    public void clear() throws DataAccessException{
        games.clear();
    }

}
