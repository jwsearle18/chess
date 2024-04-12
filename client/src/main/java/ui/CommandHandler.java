package ui;

import chess.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import websocket.WebSocketFacade;
import websocketMessages.serverMessages.ErrorMessage;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ui.DrawChessBoard.printChessBoards;

public class CommandHandler {
    private final UserInterface ui;
    private final ServerFacade httpClient;
    private Map<Integer, Integer> gameNumbertoGameID = new HashMap<>();

    public ServerFacade getHttpClient() {
        return httpClient;
    }

    public CommandHandler(UserInterface ui, int port) {
        this.ui = ui;
        this.httpClient = new ServerFacade(port);
    }
    public void handleCommand(String command, State currentState) {
        String[] parts = command.split(" ");
        String cmd = parts[0].toLowerCase();
        switch (cmd) {
            case "quit" -> System.exit(0);
            case "help" -> help(currentState);
            case "login" -> {
                if(parts.length != 3) {
                    System.out.println("Usage: login <USERNAME> <PASSWORD>");
                } else {
                    login(parts[1], parts[2]);
                }
            }
            case "register" -> {
                if (parts.length != 4) {
                    System.out.println("Usage: register <USERNAME> <PASSWORD> <EMAIL>");
                } else {
                    register(parts[1], parts[2], parts[3]);
                }
            }
            case "logout" -> {
                if (currentState == State.LOGGED_IN) {
                    logout();
                } else {
                    System.out.println("You are not logged in.");
                }
            }
            case "create" -> {
                if (currentState != State.LOGGED_IN) {
                    System.out.println("You must be logged in to create a game.");
                } else if (parts.length != 2) {
                    System.out.println("Usage: create <NAME> - a game");
                } else {
                    createGame(parts[1]);
                }
            }
            case "list" -> {
                if (currentState != State.LOGGED_IN) {
                    System.out.println("You must be logged in to list games.");
                } else {
                    listGames();
                }
            }
            case "join" -> {
                if (currentState != State.LOGGED_IN) {
                    System.out.println("You must be logged in to join a game.");
                } else if (parts.length != 3) {
                    System.out.println("Usage: join <GAME NUMBER> <COLOR>");
                } else {
                    try {
                        joinGame(Integer.parseInt(parts[1]), parts[2]);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid game number format.");
                    }
                }
            }
            case "observe" -> {
                if (currentState != State.LOGGED_IN) {
                    System.out.println("You must be logged in to observe a game.");
                } else if (parts.length != 2) {
                    System.out.println("Usage: observe <GAME NUMBER>");
                } else {
                    try {
                        observeGame(Integer.parseInt(parts[1]));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid game number format.");
                    }
                }
            }
            case "leave" -> {
                if (currentState == State.IN_GAME) {
                    ui.leaveGame();
                } else {
                    System.out.println("You are not in a game to leave.");
                }
            }
            case "move" -> {
                if (currentState != State.IN_GAME) {
                    System.out.println("You must be in a game to make a move.");
                } else if (parts.length != 3) {
                    System.out.println("Usage: move <START POSITION> <END POSITION>");
                } else {
                    if (isValidPosition(parts[1].toLowerCase()) && isValidPosition(parts[2].toLowerCase())) {
                        makeMove(parts[1].toLowerCase(), parts[2].toLowerCase());
                    } else {
                        System.out.println("Usage: move <START POSITION> <END POSITION>");
                        System.out.println("Position should be from 'a1' to 'h8'.");
                    }
                }
            }
            case "redraw" -> {
                if (currentState == State.IN_GAME) {
                    redraw();
                } else {
                    System.out.println("You can only redraw the board during a game.");
                }
            }
            case "highlight" -> {
                if (currentState == State.IN_GAME) {
                    if (parts.length != 2) {
                        System.out.println("Usage: highlight <POSITION>");
                    } else {
                        String position = parts[1].toLowerCase();
                        if (isValidPosition(position)) {
                            highlight(position);
                        } else {
                            System.out.println("Invalid position. Position should be from 'a1' to 'h8'.");
                        }
                    }
                } else {
                    System.out.println("You must be in a game to highlight moves.");
                }
            }
            default -> System.out.println("Unknown command.");
        }
    }

    private void highlight(String position) {
        ChessPosition chessPosition = convertToChessPosition(position);
        ChessGame game = ui.getCurrentGame();
        if (game != null) {
            ChessPiece piece = game.getBoard().getPiece(chessPosition);
            if (piece != null) {
                if (piece.getTeamColor() == ui.getCurrentDisplayColor()) {
                    Collection<ChessMove> moves = game.validMoves(chessPosition);
                    ui.highlightMoves(moves);
                }
                else {
                    ui.displayError("That's not your piece bucko!");
                }
            } else {
                ui.displayError("You don't have a piece there pal!");
            }
        }
    }


    private void redraw() {
        ui.redrawBoard();
    }
    private boolean isValidPosition(String position) {
        return position.matches("^[a-h][1-8]$");
    }

    private void makeMove(String start, String end) {
        try {
            ChessPosition startPos = convertToChessPosition(start);
            ChessPosition endPos = convertToChessPosition(end);
            ChessMove move = new ChessMove(startPos, endPos, null); //fix this later adding promo piece stuff

            String authToken = httpClient.getAuthToken();
            Integer currentGameID = ui.getCurrentGameID();

            if (ui.getWs() != null) {
                ui.getWs().makeMove(authToken, currentGameID, move);
            } else {
                System.out.println("WebSocket connection is not established.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            ui.displayError(e.getMessage());
        }
    }

    private ChessPosition convertToChessPosition(String pos) {
        int column = pos.charAt(0) - 'a' + 1;
        int row = Integer.parseInt(pos.substring(1));
        return new ChessPosition(row, column);
    }


    private void observeGame(int gameNumber) {
        if (!gameNumbertoGameID.containsKey(gameNumber)) {
            System.out.println("Invalid game number.");
        } else {
            Integer gameID = gameNumbertoGameID.get(gameNumber);
            if (gameID == null) {
                System.out.println("Invalid game number.");
                return;
            }
            String authToken = httpClient.getAuthToken();
            String response = httpClient.joinGame(gameID, null);
            System.out.println(response);

            if (response.contains("Successfully joined game!")) {
                ui.setCurrentState(State.CONNECTING);
                ui.setCurrentGameID(gameID);
                ui.initializeWebSocket(httpClient.getServerURL());
                ui.joinObserver(authToken, gameID);
            }
        }
    }


    private void joinGame(int gameNumber, String color) {
        Integer gameID = gameNumbertoGameID.get(gameNumber);
        if (gameID == null) {
            System.out.println("Invalid game number.");
            return;
        }
        String authToken = httpClient.getAuthToken();
        String response = httpClient.joinGame(gameID, color);
        System.out.println(response);
        if (response.contains("Successfully joined game")) {
            ui.setCurrentState(State.CONNECTING);
            ui.setCurrentGameID(gameID);
            ui.initializeWebSocket(httpClient.getServerURL());
            ChessGame.TeamColor playerColor = color.equalsIgnoreCase("white") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
            ui.joinPlayer(authToken, gameID, playerColor);
        }
    }

    private void listGames() {
        String response = httpClient.listGames();
        gameNumbertoGameID.clear();
        if (!response.startsWith("Failed to list games.")) {
            try {
                JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
                JsonArray gamesList = jsonResponse.getAsJsonArray("games");
                if (gamesList.isEmpty()) {
                    System.out.println("No games available.");
                    return;
                }
                System.out.println("Available games:");
                int number = 1;
                for (JsonElement gameElement : gamesList) {
                    JsonObject game = gameElement.getAsJsonObject();
                    int gameID = game.get("gameID").getAsInt();
                    gameNumbertoGameID.put(number, gameID);
                    String gameName = game.has("gameName") ? game.get("gameName").getAsString() : "Unnamed Game";
                    String whiteUsername = game.has("whiteUsername") && !game.get("whiteUsername").isJsonNull() ? game.get("whiteUsername").getAsString() : "Waiting for player";
                    String blackUsername = game.has("blackUsername") && !game.get("blackUsername").isJsonNull() ? game.get("blackUsername").getAsString() : "Waiting for player";
                    System.out.printf("%d. %s - White: %s, Black: %s%n", number, gameName, whiteUsername, blackUsername);
                    number++;
                }
            } catch (Exception e) {
                System.out.println("Failed to process games list.");
            }
        } else {
            System.out.println(response);
        }
    }

    private void createGame(String gameName) {
        String response = httpClient.postCreateGame(gameName);
        System.out.println(response);
    }

    private void logout() {
        String response = httpClient.postLogout();
        if(response.equals("Logout successful!")) {
            ui.setCurrentState((State.LOGGED_OUT));
            System.out.println("You have been logged out.");
        } else {
            System.out.println(response);
        }
    }

    private void login(String username, String password) {
        String response = httpClient.postLogin(username, password);
        if(response.equals("Login successful!")) {
            ui.setCurrentState(State.LOGGED_IN);
            System.out.println("Login successful. Welcome, " + username + "!");
        } else {
            System.out.println(response);
        }
    }

    private void register(String username, String password, String email) {
        String response = httpClient.postRegister(username, password, email);
        if(response.equals("Registration successful!")) {
            ui.setCurrentState(State.LOGGED_IN);
            System.out.println("Registration successful. Welcome, " + username + "!");
        } else {
            System.out.println(response);
        }
    }


    private void help(State currentState) {
        switch (currentState) {
            case LOGGED_OUT -> {
                System.out.println("""
                        register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                        login <USERNAME> <PASSWORD> - to play chess
                        quit - playing chess
                        help - with possible commands
                        """);
            }
            case LOGGED_IN -> {
                System.out.println("""
                        create <NAME> - a game
                        list - games
                        join <GAME NUMBER> [WHITE|BLACK] - a game
                        observe <GAME NUMBER> - a game
                        logout - when you are done
                        quit - playing chess
                        help - with possible commands
                        """);
            }
            case IN_GAME -> {
                System.out.println("""
                        redraw - chess board
                        highlight <POSITION> - legal moves
                        move <START POSITION> <END POSITION> - Make a chess move
                        leave - current game
                        resign - the game
                        help - with possible commands
                        """);
            }
        }
    }

}
