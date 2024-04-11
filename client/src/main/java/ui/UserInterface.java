package ui;

import chess.ChessBoard;
import chess.ChessGame;
import websocket.NotificationHandler;
import websocket.NotificationHandlerImplementation;
import websocket.WebSocketFacade;

import java.util.Scanner;
import java.util.WeakHashMap;

import static ui.EscapeSequences.*;

public class UserInterface {
    private State currentState = State.LOGGED_OUT;

    private final CommandHandler commandHandler;
    private WebSocketFacade ws;

    public UserInterface(int port) {
        this.commandHandler = new CommandHandler(this, port);
    }

    public void initializeWebSocket(String url) {
        try {
            NotificationHandler notificationHandler = new NotificationHandlerImplementation(this);
            ws = new WebSocketFacade(url, notificationHandler);
        } catch (Exception e) {
            displayError("Failed to initialize WebSocket connection: " + e.getMessage());
        }
    }

    public void start() {
        printWelcomeMessage();
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
//                if (currentState == State.IN_GAME) {
//                    //don't print prompt till after chessboard
//                } else {
                    System.out.printf("[%s] >>> ", currentState.name());

                String command = scanner.nextLine().trim();
                commandHandler.handleCommand(command, currentState);
//                if (currentState == State.IN_GAME) {
//                    System.out.printf("[%s] >>> ", currentState.name());
//                }
            }
        }
    }

    public void setCurrentState(State newState) {
        this.currentState = newState;
    }

    private void printWelcomeMessage() {
        System.out.println("Welcome to 240 Chess! Type 'help' to get started.");
    }

    public void joinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor) {
        try {
            if (ws == null) {
                displayError("WebSocket is not initialized.");
                return;
            }
            ws.joinPlayer(authToken, gameID, playerColor);
        } catch (Exception e) {
            displayError("Error sending join command: " + e.getMessage());
        }
    }

    public void updateChessBoard(ChessGame game, ChessGame.TeamColor playerColor) {
        ChessBoard board = game.getBoard();
        boolean whiteAtBottom = playerColor == ChessGame.TeamColor.WHITE;
        DrawChessBoard.printChessBoards(System.out, board, whiteAtBottom);
        if (currentState == State.IN_GAME) {
            System.out.printf("[%s] >>> ", currentState.name());
        }
    }

    public void displayError(String errorMessage) {
        System.out.println(errorMessage);
    }

    public void displayNotification(String notificationMessage) {
        System.out.println(notificationMessage);
    }
}
