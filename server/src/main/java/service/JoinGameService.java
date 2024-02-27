package service;

import Failures.F400;
import Failures.F401;
import Failures.F403;
import chess.ChessGame;
import chess.ChessPiece;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import requests.JoinGameRequest;
import results.JoinGameResult;

public class JoinGameService {
    public void joinGame(JoinGameRequest joinGameRequest) throws DataAccessException, F400, F401, F403 {
        GameDAO gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();

        AuthData authData = authDAO.getAuth(joinGameRequest.authToken());
        GameData gameData = gameDAO.getGame((joinGameRequest.gameID()));
        ChessGame.TeamColor playerColor = joinGameRequest.playerColor();

        if(authData == null){
            throw new F401("Error: unauthorized");
        } else if(gameData == null) {
            throw new F400("Error: bad request");
        } else if(gameData.blackUsername() != null && playerColor == ChessGame.TeamColor.BLACK) {
            throw new F403("Error: already taken");
        } else if(gameData.whiteUsername() != null && playerColor == ChessGame.TeamColor.WHITE) {
            throw new F403("Error: already taken");
        } else if(playerColor == null) {
            //add as observer
            return;
        } else {
//            String playerName =
//            GameData newGameData = new GameData()
//            gameDAO.updateGame(newGameData);
            return;
        }
    }
}
