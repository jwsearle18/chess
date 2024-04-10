import ui.UserInterface;

public class ClientMain {


    public static void main(String[] args) {
        int desiredPort = 4040;
        if (args.length > 0) {
            desiredPort = Integer.parseInt(args[0]);
        }
        System.out.println("Connecting to server on port: " + desiredPort);
        UserInterface ui = new UserInterface(desiredPort);
        ui.start();
    }
}