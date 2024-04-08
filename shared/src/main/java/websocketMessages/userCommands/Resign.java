package websocketMessages.userCommands;

public class Resign extends UserGameCommand {
    public Resign(String authToken, int gameID) {
        super(authToken);
        this.commandType = CommandType.RESIGN;

    }
}
