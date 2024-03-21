package ui;

import java.util.Scanner;
import java.util.WeakHashMap;

public class UserInterface {
    private State currentState = State.LOGGED_OUT;

    private final CommandHandler commandHandler = new CommandHandler(this);

    public void start() {
        printWelcomeMessage();
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.printf("[%s] >>> ", currentState.name());
                String command = scanner.nextLine().trim();
                commandHandler.handleCommand(command, currentState);
            }
        }
    }

    public void setCurrentState(State newState) {
        this.currentState = newState;
    }

    private void printWelcomeMessage() {
        System.out.println("Welcome to 240 Chess! Type 'help' to get started.");
    }
}
