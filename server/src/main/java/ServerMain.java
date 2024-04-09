import chess.*;
import server.Server;
public class ServerMain {

    public static void main(String[] args) {
        int desiredPort = 4040;
        if (args.length > 0) {
            desiredPort = Integer.parseInt(args[0]);
        }
        Server server = new Server();
        server.run(desiredPort);

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
    }
}