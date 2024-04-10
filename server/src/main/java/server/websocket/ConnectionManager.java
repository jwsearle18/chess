package server.websocket;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final ConcurrentHashMap<Integer, Set<Connection>> gameSessions = new ConcurrentHashMap<>();
    public void addConnection(int gameID, String playerName, Session session) {
        Connection newConnection = new Connection(playerName, session, gameID);
        gameSessions.computeIfAbsent(gameID, k -> ConcurrentHashMap.newKeySet()).add(newConnection);
    }

    public void removeConnection(int gameID, String playerName) {
        Set<Connection> connections = gameSessions.getOrDefault(gameID, ConcurrentHashMap.newKeySet());
        connections.removeIf(conn -> conn.playerName.equals(playerName));
        if (connections.isEmpty()) {
            gameSessions.remove(gameID);
        }
    }

    public void broadcast(int gameID, String excludePlayerName, String message) {
        Set<Connection> connections = gameSessions.getOrDefault(gameID, ConcurrentHashMap.newKeySet());
        for (Connection conn : connections) {
            if (!conn.playerName.equals(excludePlayerName)) {
                try {
                    conn.send(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
