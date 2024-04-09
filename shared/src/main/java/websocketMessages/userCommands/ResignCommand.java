package websocketMessages.userCommands;

public class ResignCommand extends UserGameCommand {
    public ResignCommand (String authToken, int gameID) {
        super(authToken);
        this.commandType = CommandType.RESIGN;

    }
}
