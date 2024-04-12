package ui;

import chess.ChessBoard;
import chess.ChessGame;
import websocket.NotificationHandler;
import websocket.NotificationHandlerImplementation;
import websocket.WebSocketFacade;

import java.io.IOException;
import java.util.Scanner;
import java.util.WeakHashMap;

import static ui.EscapeSequences.*;

public class UserInterface {
    private State currentState = State.LOGGED_OUT;
    private Integer currentGameID = null;

    private final CommandHandler commandHandler;
    private WebSocketFacade ws;

    private ChessGame currentGame;
    private ChessGame.TeamColor playerColor;
    public void setCurrentDisplayColor(ChessGame.TeamColor playerColor) {
        this.playerColor = playerColor;
    }

    public ChessGame.TeamColor getCurrentDisplayColor() {
        return playerColor;
    }

    public void setCurrentGame(ChessGame game) {
        this.currentGame = game;
    }

    public ChessGame getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGameID(Integer gameID) {
        this.currentGameID = gameID;
    }

    public WebSocketFacade getWs() {
        return ws;
    }

    public Integer getCurrentGameID() {
        return currentGameID;
    }


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
        tick();
    }

    public void setCurrentState(State newState) {
        currentState = newState;
    }

    private void printWelcomeMessage() {
        System.out.println("Welcome to 240 Chess! Type 'help' to get started.");
    }

    public void tick() {

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                // If not connecting, prompt for the next command
                if (currentState != State.CONNECTING) {
                    promptNextCommand();
                }

                // Process user input
                String command = scanner.nextLine().trim();
                commandHandler.handleCommand(command, currentState);

                while (currentState == State.CONNECTING) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    public void leaveGame() {
        if (currentGameID == null) {
            displayError("You are not currently in a game.");
        }
        try {
            if (ws == null) {
                displayError("WebSocket is not initialized.");
                return;
            }
            String authToken = commandHandler.getHttpClient().getAuthToken();
            ws.leaveGame(authToken, currentGameID);
            setCurrentState(State.LOGGED_IN);
            setCurrentGameID(null);
            System.out.println("You have left the game.");
        } catch (Exception e) {
            displayError(e.getMessage());
        }
    }

    public void joinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor) {
        try {
            if (ws == null) {
                displayError("WebSocket is not initialized.");
                return;
            }
            setCurrentDisplayColor(playerColor);
            ws.joinPlayer(authToken, gameID, playerColor);
        } catch (Exception e) {
            displayError("Error sending join command: " + e.getMessage());
        }
    }

    public void joinObserver(String authToken, int gameID) {
        try {
            if (ws == null) {
                displayError("WebSocket is not initialized.");
                return;
            }
            setCurrentDisplayColor(ChessGame.TeamColor.WHITE);
            ws.joinObserver(authToken, gameID);
            System.out.println("You are now observing the game.");
        } catch (Exception e) {
            displayError("Error sending observe command: " + e.getMessage());
        }
    }

    public void redrawBoard() {

        ChessGame currentGame = getCurrentGame();
        if (currentGame != null) {
            updateChessBoard(currentGame, getCurrentDisplayColor());
        } else {
            displayError("No game is currently in progress to redraw.");
        }
    }

    public void updateChessBoard(ChessGame game, ChessGame.TeamColor playerColor) {



        ChessBoard board = game.getBoard();
        boolean whiteAtBottom = getCurrentDisplayColor() == ChessGame.TeamColor.WHITE;
        DrawChessBoard.printChessBoards(System.out, board, whiteAtBottom);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


    }
    private void promptNextCommand() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.printf("[%s] >>> ", currentState.name());
    }

    public void displayError(String errorMessage) {
        System.out.println("\n" + errorMessage);
    }

    public void displayNotification(String notificationMessage) {
        System.out.println("\n" + notificationMessage);
        System.out.printf("[%s] >>> ", currentState.name());
    }

}
