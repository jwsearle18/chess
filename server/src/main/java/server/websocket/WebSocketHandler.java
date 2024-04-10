package server.websocket;

import java.io.IOException;
import java.util.Objects;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import com.google.gson.Gson;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocketMessages.serverMessages.ErrorMessage;
import websocketMessages.serverMessages.LoadGameMessage;
import websocketMessages.serverMessages.NotificationMessage;
import websocketMessages.userCommands.*;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final Gson gson = new Gson();
    private final AuthDAO authDAO = new SQLAuthDAO();
    private final GameDAO gameDAO = new SQLGameDAO();

    public WebSocketHandler() throws DataAccessException {
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);

        switch (command.getCommandType()) {
            case JOIN_PLAYER -> handleJoinPlayer(session, message);
            case JOIN_OBSERVER -> handleJoinObserver(session, message);
            case MAKE_MOVE -> handleMakeMove(session, message);
            case RESIGN -> handleResign(session, message);
            case LEAVE -> handleLeave(session, message);
        }
    }

    private void handleJoinPlayer(Session session, String message) throws IOException {
        JoinPlayerCommand joinCommand = gson.fromJson(message, JoinPlayerCommand.class);

        try {
            String authToken = joinCommand.getAuthString();
            AuthData authData = authDAO.getAuth(authToken);
            if (authData == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Invalid auth token.");
                session.getRemote().sendString(gson.toJson(errorMessage));
                return;
            }
            String playerName = authData.username();

            GameData gameData = gameDAO.getGame(joinCommand.getGameID());
            if (gameData == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Game not found.");
                session.getRemote().sendString(gson.toJson(errorMessage));
                return;
            }

            boolean canJoin = false;
            if (joinCommand.getPlayerColor() == ChessGame.TeamColor.WHITE) {
                if (gameData.whiteUsername().equals(playerName)) {
                    canJoin = true;
                }
            } else if (joinCommand.getPlayerColor() == ChessGame.TeamColor.BLACK) {
                if (gameData.blackUsername().equals(playerName)) {
                    canJoin = true;
                }
            }

            if (!canJoin) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Wrong team.");
                session.getRemote().sendString(gson.toJson(errorMessage));
                return;
            }

            connections.addConnection(joinCommand.getGameID(), playerName, session);
            NotificationMessage notificationMessage = new NotificationMessage(String.format("%s has joined as %s.", playerName, joinCommand.getPlayerColor().toString()));
            connections.broadcast(joinCommand.getGameID(), playerName, gson.toJson(notificationMessage));

            LoadGameMessage loadGameMessage = new LoadGameMessage(gameData.game(), joinCommand.getPlayerColor());
            session.getRemote().sendString(gson.toJson(loadGameMessage));

        }
        catch (DataAccessException e) {
            ErrorMessage errorMessage = new ErrorMessage("Database access error.");
            session.getRemote().sendString(gson.toJson(errorMessage));
        }
        catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage("An unexpected error occurred.");
            session.getRemote().sendString(gson.toJson(errorMessage));
        }
    }

    private void handleJoinObserver(Session session, String message) throws IOException {
        JoinObserverCommand joinObserverCommand = gson.fromJson(message, JoinObserverCommand.class);

        try {
            String authToken = joinObserverCommand.getAuthString();
            AuthData authData = authDAO.getAuth(authToken);
            if (authData == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Invalid auth token.");
                session.getRemote().sendString(gson.toJson(errorMessage));
                return;
            }
            String observerName = authData.username();

            GameData gameData = gameDAO.getGame(joinObserverCommand.getGameID());
            if(gameData == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Game not found.");
                session.getRemote().sendString(gson.toJson(errorMessage));
                return;
            }
            connections.addConnection(joinObserverCommand.getGameID(), observerName, session);
            NotificationMessage notificationMessage = new NotificationMessage(String.format("%s has joined as an observer.", observerName));
            connections.broadcast(joinObserverCommand.getGameID(), observerName, gson.toJson(notificationMessage));

            LoadGameMessage loadGameMessage = new LoadGameMessage(gameData.game(), null);
            session.getRemote().sendString(gson.toJson(loadGameMessage));

        } catch (DataAccessException e) {
            ErrorMessage errorMessage = new ErrorMessage("Database access error.");
            session.getRemote().sendString(gson.toJson(errorMessage));
        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage("An unexpected error occurred.");
            session.getRemote().sendString(gson.toJson(errorMessage));
        }
    }

    public void handleMakeMove (Session session, String message) throws IOException {
        MakeMoveCommand makeMoveCommand = gson.fromJson(message, MakeMoveCommand.class);

        try {
            String authToken = makeMoveCommand.getAuthString();
            AuthData authData = authDAO.getAuth(authToken);
            if (authData == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Invalid auth token.");
                session.getRemote().sendString(gson.toJson(errorMessage));
                return;
            }
            String playerName = authData.username();

            GameData gameData = gameDAO.getGame(makeMoveCommand.getGameID());
            if(gameData == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Game not found.");
                session.getRemote().sendString(gson.toJson(errorMessage));
                return;
            }

            ChessGame game = gameData.game();
            ChessGame.TeamColor playerColor = null;
            if(playerName.equals(gameData.whiteUsername())) {
                playerColor = ChessGame.TeamColor.WHITE;
            } else if (playerName.equals(gameData.blackUsername())) {
                playerColor = ChessGame.TeamColor.BLACK;
            }

            if (playerColor == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Can't make move as an observer.");
                session.getRemote().sendString(gson.toJson(errorMessage));
                return;
            }
            if (playerColor != game.getTeamTurn()) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Not your turn pal.");
                session.getRemote().sendString(gson.toJson(errorMessage));
                return;
            }
            if (!game.validMoves(makeMoveCommand.getMove().getStartPosition()).contains(makeMoveCommand.getMove())) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Invalid move, Sarge, try again perhaps.");
                session.getRemote().sendString(gson.toJson(errorMessage));
                return;
            }

            game.makeMove(makeMoveCommand.getMove());
            gameDAO.updateGame(new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game));

            LoadGameMessage loadGameMessage = new LoadGameMessage(game, null);
            connections.broadcast(makeMoveCommand.getGameID(), null, gson.toJson(loadGameMessage));

            NotificationMessage notificationMessage = new NotificationMessage(String.format("%s made a move: %s", playerName, makeMoveCommand.getMove().toString()));
            connections.broadcast(makeMoveCommand.getGameID(), playerName, gson.toJson(notificationMessage));

        } catch (DataAccessException e) {
            ErrorMessage errorMessage = new ErrorMessage("Database access error.");
            session.getRemote().sendString(gson.toJson(errorMessage));
        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage("An unexpected error occurred.");
            session.getRemote().sendString(gson.toJson(errorMessage));
        }
    }


    public void handleResign(Session session, String message) throws IOException {
        ResignCommand resignCommand = gson.fromJson(message, ResignCommand.class);

        try {
            String authToken = resignCommand.getAuthString();
            AuthData authData = authDAO.getAuth(authToken);
            if (authData == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Invalid auth token.");
                session.getRemote().sendString(gson.toJson(errorMessage));
                return;
            }
            String playerName = authData.username();

            GameData gameData = gameDAO.getGame(resignCommand.getGameID());
            if(gameData == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Game not found.");
                session.getRemote().sendString(gson.toJson(errorMessage));
                return;
            }

            if (gameData.game().getGameStatus() == ChessGame.GameStatus.INACTIVE) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Game not Active.");
                session.getRemote().sendString(gson.toJson(errorMessage));
                return;
            }

            if (!playerName.equals(gameData.whiteUsername()) && !playerName.equals(gameData.blackUsername())) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Observers cannot resign.");
                session.getRemote().sendString(gson.toJson(errorMessage));
                return;
            }

            ChessGame newInactiveGame = gameData.game();
            newInactiveGame.setGameStatus(ChessGame.GameStatus.INACTIVE);
            gameDAO.updateGame(new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), newInactiveGame));


            connections.removeConnection(gameData.gameID(), playerName);
            NotificationMessage notificationMessage = new NotificationMessage(String.format("%s has resigned.", playerName));
            connections.broadcast(resignCommand.getGameID(), playerName, gson.toJson(notificationMessage));

        } catch (DataAccessException e) {
            ErrorMessage errorMessage = new ErrorMessage("Database access error.");
            session.getRemote().sendString(gson.toJson(errorMessage));
        }
    }

    public void handleLeave(Session session, String message) throws IOException {
        LeaveCommand leaveCommand = gson.fromJson(message, LeaveCommand.class);

        try {
            String authToken = leaveCommand.getAuthString();
            AuthData authData = authDAO.getAuth(authToken);
            if (authData == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Invalid auth token.");
                session.getRemote().sendString(gson.toJson(errorMessage));
                return;
            }
            String playerName = authData.username();

            GameData gameData = gameDAO.getGame(leaveCommand.getGameID());
            if(gameData == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: Game not found.");
                session.getRemote().sendString(gson.toJson(errorMessage));
                return;
            }
            String whiteName = gameData.whiteUsername();
            String blackName = gameData.blackUsername();
            if (Objects.equals(playerName, whiteName)) {
                GameData whiteLeftGame = new GameData(gameData.gameID(), null, blackName, gameData.gameName(), gameData.game());
                gameDAO.updateGame(whiteLeftGame);
            } else if (Objects.equals(playerName, blackName)) {
                GameData blackLeftGame = new GameData(gameData.gameID(), whiteName, null, gameData.gameName(), gameData.game());
                gameDAO.updateGame(blackLeftGame);
            }
            connections.removeConnection(gameData.gameID(), playerName);
            NotificationMessage notificationMessage = new NotificationMessage(String.format("%s has left the game.", playerName));
            connections.broadcast(leaveCommand.getGameID(), playerName, gson.toJson(notificationMessage));

        } catch (DataAccessException e) {
            ErrorMessage errorMessage = new ErrorMessage("Database access error.");
            session.getRemote().sendString(gson.toJson(errorMessage));
        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage("An unexpected error occurred.");
            session.getRemote().sendString(gson.toJson(errorMessage));
        }
    }


}
