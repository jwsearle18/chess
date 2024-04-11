package websocket;


import websocketMessages.serverMessages.LoadGameMessage;
import websocketMessages.serverMessages.NotificationMessage;
import websocketMessages.serverMessages.ErrorMessage;

public interface NotificationHandler {
    void handleLoadGame(LoadGameMessage message);
    void handleError(ErrorMessage message);
    void handleNotification(NotificationMessage message);

}
