package websocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand {
    private final int gameID;


    public JoinObserverCommand(String authToken, int gameID) {
        super(authToken);
        commandType = CommandType.JOIN_PLAYER;
        this.gameID = gameID;

    }

    public int getGameID() { return gameID; }
}
