package websocketMessages.serverMessages;

import chess.ChessGame;
import model.GameData;

public class LoadGameMessage extends ServerMessage {
    private final ChessGame game;
    private final ChessGame.TeamColor playerColor;
    public LoadGameMessage(ChessGame game, ChessGame.TeamColor playerColor) {
        super(ServerMessageType.LOAD_GAME);
        this.playerColor = playerColor;
        this.game = game;
    }
    public ChessGame getGame() {
        return game;
    }
    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}
