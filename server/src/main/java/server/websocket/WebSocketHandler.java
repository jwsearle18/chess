package server.websocket;

import java.io.IOException;

import Failures.F400;
import Failures.F401;
import Failures.F403;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import requests.JoinGameRequest;
import service.JoinGameService;
import websocketMessages.serverMessages.ServerMessage;
import websocketMessages.userCommands.JoinPlayerCommand;
import websocketMessages.userCommands.UserGameCommand;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private JoinGameService joinGameService = new JoinGameService();
    private final Gson gson = new Gson();

    public WebSocketHandler(JoinGameService joinGameService) {
        this.joinGameService = joinGameService;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand userGameCommand = gson.fromJson(message, UserGameCommand.class);

        switch (userGameCommand.getCommandType()) {
            case JOIN_PLAYER -> {
                handleJoinPlayer(userGameCommand, session);
            }
        }
    }

    private void handleJoinPlayer(UserGameCommand command, Session session) throws IOException {
        if (!(command instanceof JoinPlayerCommand joinCommand)) {
            sendError(session, "Invalid command format.");
            return;
        }

        JoinGameRequest joinGameRequest = new JoinGameRequest(
                joinCommand.getAuthString(),
                joinCommand.getTeamColor(),
                joinCommand.getGameID());

        try {
            String playerName = joinGameService.joinGame(joinGameRequest);

            ServerMessage loadGameMessage = new ServerMessage(
                    ServerMessage.ServerMessageType.LOAD_GAME,
                    "Game loaded successfully.");
            session.getRemote().sendString(gson.toJson(loadGameMessage));

            String notificationMessage = String.format("%s joined the game as %s.", playerName, joinCommand.getTeamColor().toString());
            ServerMessage notification = new ServerMessage(
                    ServerMessage.ServerMessageType.NOTIFICATION,
                    notificationMessage);

            // Broadcast to all other clients in the game
            connections.broadcastToGame(joinCommand.getGameID(), playerName, gson.toJson(notification));

        } catch (F400 | F401 | F403 | DataAccessException e) {
            sendError(session, "Error: " + e.getMessage());
        }
    }

    private void sendError(Session session, String message) throws IOException {
        ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, message);
        session.getRemote().sendString(gson.toJson(errorMessage));
    }
}
