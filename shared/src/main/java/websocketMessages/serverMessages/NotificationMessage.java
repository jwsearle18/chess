package websocketMessages.serverMessages;

public class NotificationMessage extends ServerMessage {
    private final String message;

    public NotificationMessage(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.serverMessageType = ServerMessageType.NOTIFICATION;
        this.message = message;
    }

    public String getNotification() {
        return message;
    }
}
