package ui;

import chess.ChessBoard;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

import static ui.DrawChessBoard.printChessBoards;

public class CommandHandler {
    private final UserInterface ui;
    private final HttpClient httpClient;
    private Map<Integer, Integer> gameNumbertoGameID = new HashMap<>();

    public CommandHandler(UserInterface ui, int port) {
        this.ui = ui;
        this.httpClient = new HttpClient(port);
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
                        int gameNumber = Integer.parseInt(parts[1]);
                        String color = parts[2];
                        joinGame(gameNumber, color);
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
                        int gameNumber = Integer.parseInt(parts[1]);
                        observeGame(gameNumber);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid game number format.");
                    }
                }
            }
            default -> System.out.println("Unknown command.");
        }
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

            String response = httpClient.joinGame(gameID, null);
            System.out.println(response);

            if (response.contains("Successfully joined game!")) {
                ChessBoard board = new ChessBoard();
                board.resetBoard();
                printChessBoards(System.out, board, true);
            }
        }
    }


    private void joinGame(int gameNumber, String color) {
        Integer gameID = gameNumbertoGameID.get(gameNumber);
        if (gameID == null) {
            System.out.println("Invalid game number.");
            return;
        }

        String response = httpClient.joinGame(gameID, color);
        System.out.println(response);
        if (response.contains("Successfully joined game")) {
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            boolean whiteAtBottom = color.equalsIgnoreCase("white");
            printChessBoards(System.out, board, whiteAtBottom);
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
                        join <ID> [WHITE|BLACK] - a game
                        observe <ID> - a game
                        logout - when you are done
                        quit - playing chess
                        help - with possible commands
                        """);
            }
        }
    }

}
