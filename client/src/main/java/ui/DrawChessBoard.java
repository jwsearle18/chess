package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;

import static ui.EscapeSequences.*;
import java.util.Random;

public class DrawChessBoard {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    public static void draw(PrintStream out, ChessBoard board, boolean whiteAtBottom) {
        out.print(ERASE_SCREEN);

        drawBoard(out, board, whiteAtBottom);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(moveCursorToLocation(0, BOARD_SIZE_IN_SQUARES + 3));
    }



    public static void drawBoard(PrintStream out, ChessBoard board, boolean whiteAtBottom) {
        for (int row = 0; row < BOARD_SIZE_IN_SQUARES; row++) {
            for (int col = 0; col < BOARD_SIZE_IN_SQUARES; col++) {
                    int actualRow = whiteAtBottom ? 7 - row : row;
                    ChessPiece piece = board.getPiece(new ChessPosition(actualRow + 1, col + 1));
                    drawSquare(out, row, col, whiteAtBottom, piece);
            }
            out.println();
        }
    }

    public static void drawSquare(PrintStream out, int row, int col, boolean whiteAtBottom, ChessPiece piece) {
        boolean isLightSquare = (row + col) % 2 == 0;
        String bgColor = isLightSquare ? SET_BG_COLOR_WHITE : SET_BG_COLOR_LIGHT_GREY;
        out.print(bgColor);

        String pieceSymbol = piece != null ? getPieceSymbol(piece) : EMPTY;
        printPiece(out, pieceSymbol);
    }

    private static String getPieceSymbol(ChessPiece piece) {

        return SET_TEXT_COLOR_BLACK + switch (piece.getPieceType()) {
            case KING -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KING : BLACK_KING;
            case QUEEN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_QUEEN : BLACK_QUEEN;
            case BISHOP -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KNIGHT : BLACK_KNIGHT;
            case ROOK -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_ROOK : BLACK_ROOK;
            case PAWN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_PAWN : BLACK_PAWN;
            default -> EMPTY;
        } + RESET_TEXT_COLOR;
    }

    private static void printPiece(PrintStream out, String piece) {
        out.print(piece);
        out.print(RESET_BG_COLOR);
    }
}
