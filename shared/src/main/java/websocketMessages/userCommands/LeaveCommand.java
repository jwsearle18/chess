package websocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand {
    private final int gameID;


    public LeaveCommand(String authToken, int gameID) {
        super(authToken);
        commandType = CommandType.LEAVE;
        this.gameID = gameID;

    }

    public int getGameID() { return gameID; }
}
