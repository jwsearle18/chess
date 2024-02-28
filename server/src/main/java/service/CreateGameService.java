package service;

import Failures.F400;
import Failures.F401;
import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import requests.CreateGameRequest;
import requests.GameName;
import results.CreateGameResult;

import java.util.Random;

public class CreateGameService {

    public static CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException, F400, F401{
        GameDAO gameDAO  = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();

        AuthData authData = authDAO.getAuth(createGameRequest.authToken());
        if(authData == null) {
            throw new F401("Error: unauthorized");
        } else if(createGameRequest.gameName() == null) {
            throw new F400("Error: bad request");
        } else {
            Random randomNumber = new Random();
            int n = randomNumber.nextInt(1000000);
            GameData gameData = new GameData(n, null, null, createGameRequest.gameName(), new ChessGame());
            gameDAO.createGame(gameData);
            return new CreateGameResult(gameData.gameID());
        }

    }
}
