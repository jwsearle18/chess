package ui;

public class CommandHandler {
    private final UserInterface ui;
    private final HttpClient httpClient = new HttpClient();

    public CommandHandler(UserInterface ui) {
        this.ui = ui;
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
//            case "create game" -> createGame();
//            case "list games" -> listGames();
//            case "join game" -> joinGame();
//            case "join observer" -> joinObserver();
            default -> System.out.println("Unknown command.");
        }
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
                        join <ID> [WHITE|BLACK|<empty>] - a game
                        observe <ID> - a game
                        logout - when you are done
                        quit - playing chess
                        help - with possible commands
                        """);
            }
        }
    }

}
