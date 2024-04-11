package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import ui.WebSocketEventCallbacks;
import websocketMessages.serverMessages.ErrorMessage;
import websocketMessages.serverMessages.LoadGameMessage;
import websocketMessages.serverMessages.NotificationMessage;
import websocketMessages.serverMessages.ServerMessage;
import websocketMessages.userCommands.JoinObserverCommand;
import websocketMessages.userCommands.JoinPlayerCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;


    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws Exception {

        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    handleMessage(message);
                }
            });

        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new Exception(e.getMessage());
        }
    }
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    private void handleMessage(String message) {
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
        switch (serverMessage.getServerMessageType()) {
            case LOAD_GAME -> {
                LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
                notificationHandler.handleLoadGame(loadGameMessage);
            }
            case ERROR -> {
                ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                notificationHandler.handleError(errorMessage);
            }
            case NOTIFICATION -> {
                NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
                notificationHandler.handleNotification(notificationMessage);
            }
        }
    }

    public void joinPlayer(String authToken, Integer gameID, ChessGame.TeamColor playerColor) throws IOException {
        var joinPlayerCommand = new JoinPlayerCommand(authToken, gameID, playerColor);
        this.session.getBasicRemote().sendText(new Gson().toJson(joinPlayerCommand));
    }

    public void joinObserver(String authToken, Integer gameID) throws IOException {
        var joinObserverCommand = new JoinObserverCommand(authToken, gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(joinObserverCommand));

    }

    public void closeSession() throws IOException {
        if (this.session != null) {
            this.session.close();
        }
    }
}
