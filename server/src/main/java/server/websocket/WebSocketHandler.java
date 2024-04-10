package server.websocket;

import java.io.IOException;

import chess.ChessGame;
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
import websocketMessages.userCommands.JoinPlayerCommand;
import websocketMessages.userCommands.UserGameCommand;

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
            case JOIN_PLAYER -> {
                handleJoinPlayer(session, message);
            }
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

    } catch (DataAccessException e) {
        ErrorMessage errorMessage = new ErrorMessage("Database access error.");
        session.getRemote().sendString(gson.toJson(errorMessage));
    } catch (Exception e) {
        ErrorMessage errorMessage = new ErrorMessage("An unexpected error occurred.");
        session.getRemote().sendString(gson.toJson(errorMessage));
    }
}

}
