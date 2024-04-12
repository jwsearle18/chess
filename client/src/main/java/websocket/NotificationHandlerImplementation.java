package websocket;

import chess.ChessGame;
import ui.State;
import ui.UserInterface;
import websocketMessages.serverMessages.ErrorMessage;
import websocketMessages.serverMessages.LoadGameMessage;
import websocketMessages.serverMessages.NotificationMessage;

public class NotificationHandlerImplementation implements NotificationHandler{
    private final UserInterface ui;

    public NotificationHandlerImplementation(UserInterface ui) {
        this.ui = ui;
    }
    @Override
    public void handleLoadGame(LoadGameMessage message) {
//        ChessGame.TeamColor displayColor = message.getPlayerColor();
//        if (displayColor == null) {
//            displayColor = ChessGame.TeamColor.WHITE;
//        }
        ChessGame.TeamColor displayColor = ui.getCurrentDisplayColor();
        ui.setCurrentGame(message.getGame());
        ui.updateChessBoard(message.getGame(), displayColor);
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
