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

import java.util.Objects;

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
        } else if(!Objects.equals(gameData.blackUsername(), "") && playerColor == ChessGame.TeamColor.BLACK) {
            throw new F403("Error: already taken");
        } else if(!Objects.equals(gameData.whiteUsername(), "") && playerColor == ChessGame.TeamColor.WHITE) {
            throw new F403("Error: already taken");
        } else if(playerColor == null) {
            //add as observer
            return;
        } else if(playerColor == ChessGame.TeamColor.BLACK){
            String playerName = authData.username();
            GameData newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), playerName, gameData.gameName(), gameData.game());
            gameDAO.updateGame(newGameData);
            return;
        } else if(playerColor == ChessGame.TeamColor.WHITE){
            String playerName = authData.username();
            GameData newGameData = new GameData(gameData.gameID(), playerName, gameData.blackUsername(), gameData.gameName(), gameData.game());
            gameDAO.updateGame(newGameData);
            return;
        }
    }
}
