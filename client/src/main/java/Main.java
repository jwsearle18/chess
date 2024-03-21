import chess.*;
import ui.DrawChessBoard;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.DrawChessBoard.*;
import static ui.EscapeSequences.*;

public class Main {

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        ChessBoard board = new ChessBoard();
        board.resetBoard();

        draw(out, board, false);
        out.print("\n\n");
        draw(out, board, true);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);


    }
}