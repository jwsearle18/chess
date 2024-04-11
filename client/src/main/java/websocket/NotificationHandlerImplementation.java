package websocket;

import ui.State;
import ui.UserInterface;
import websocketMessages.serverMessages.ErrorMessage;
import websocketMessages.serverMessages.LoadGameMessage;
import websocketMessages.serverMessages.NotificationMessage;

public class NotificationHandlerImplementation implements NotificationHandler{
    private UserInterface ui;

    public NotificationHandlerImplementation(UserInterface ui) {
        this.ui = ui;
    }
    @Override
    public void handleLoadGame(LoadGameMessage message) {
        ui.updateChessBoard(message.getGame(), message.getPlayerColor());
        ui.setCurrentState(State.IN_GAME);
    }
    @Override
    public void handleError(ErrorMessage message) {
        ui.displayError(message.getErrorMessage());
    }
    @Override
    public void handleNotification(NotificationMessage message) {
        ui.displayNotification(message.getNotification());
    }
}
