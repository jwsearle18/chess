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

import static java.util.Objects.isNull;

public class JoinGameService {
    public void joinGame(JoinGameRequest joinGameRequest) throws DataAccessException, F400, F401, F403 {
        GameDAO gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        if(isNull(authDAO.getAuth(joinGameRequest.authToken()))){
            throw new F401("Error: unauthorized");
        }

        String playerName = authDAO.getAuth(joinGameRequest.authToken()).username();
        GameData gameData = gameDAO.getGame((joinGameRequest.gameID()));
        ChessGame.TeamColor playerColor = joinGameRequest.playerColor();


        if(isNull(gameData)) {
            throw new F400("Error: bad request");
        }
        else if(!isNull(playerColor)) {
            if(playerColor.equals(ChessGame.TeamColor.BLACK) && !Objects.equals(gameData.blackUsername(), "")) {
                throw new F403("Error: already taken");
            } else {
                if(playerColor.equals(ChessGame.TeamColor.BLACK)) {
//                    String playerName = authData.username();
                    GameData newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), playerName, gameData.gameName(), gameData.game());
                    gameDAO.updateGame(newGameData);
                }
            }
            if(playerColor.equals(ChessGame.TeamColor.WHITE) && !Objects.equals(gameData.whiteUsername(), "")) {

                throw new F403("Error: already taken");
            } else {
                if(playerColor.equals(ChessGame.TeamColor.WHITE)) {
//                    String playerName = authData.username();
                    GameData newGameData = new GameData(gameData.gameID(), playerName, gameData.blackUsername(), gameData.gameName(), gameData.game());
                    gameDAO.updateGame(newGameData);
                }
            }
        } else {
            //add observer
            return;
        }

//        else if(!Objects.equals(gameData.blackUsername(), "") && playerColor == ChessGame.TeamColor.BLACK) {
//            throw new F403("Error: already taken");
//        } else if(!Objects.equals(gameData.whiteUsername(), "") && playerColor == ChessGame.TeamColor.WHITE) {
//            throw new F403("Error: already taken");
//        } else if(playerColor == null) {
//            //add as observer
//            return;
//        } else if(playerColor == ChessGame.TeamColor.BLACK){
//            String playerName = authData.username();
//            if(playerName != null) {
//                GameData newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), playerName, gameData.gameName(), gameData.game());
//                gameDAO.updateGame(newGameData);
//                return;
//            } else {
//                throw new F400("Error: bad request");
//            }
//        } else if(playerColor == ChessGame.TeamColor.WHITE){
//            String playerName = authData.username();
//            if(playerName != null) {
//                GameData newGameData = new GameData(gameData.gameID(), playerName, gameData.blackUsername(), gameData.gameName(), gameData.game());
//                gameDAO.updateGame(newGameData);
//                return;
//            } else {
//                throw new F400("Error: bad request");
//            }
//        }
    }
}
