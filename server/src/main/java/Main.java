import chess.*;
import server.Server;
public class Main {


    public static void main(String[] args) {

        Server server = new Server();
        server.run(Integer.parseInt(args[0]));

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
    }
}