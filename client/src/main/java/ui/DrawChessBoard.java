package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;

import static ui.EscapeSequences.*;

public class DrawChessBoard {

    private static final int BOARD_SIZE_IN_SQUARES = 8;

    public static void printChessBoards(PrintStream out, ChessBoard board, boolean whiteAtBottom) {
        if (whiteAtBottom) {
            draw(out, board, false);
            out.print("\n");
            draw(out, board, true);
        } else {
            draw(out, board, true);
            out.print("\n");
            draw(out, board, false);
        }
    }
    public static void draw(PrintStream out, ChessBoard board, boolean whiteAtBottom) {
        out.print(ERASE_SCREEN);
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);

        drawBoard(out, board, whiteAtBottom);
        out.print(moveCursorToLocation(0, BOARD_SIZE_IN_SQUARES + 3));
    }

    public static void printColumnLabel(PrintStream out, boolean whiteAtBottom) {
        String[] colLabelsW = {EMPTY, " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", EMPTY};
        String[] colLabelsB = {EMPTY, " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a ", EMPTY};
        if(whiteAtBottom) {
            for (String colLabel : colLabelsW) {
                out.print(SET_BG_COLOR_LIGHT_GREY + colLabel + RESET_BG_COLOR);
            }
            out.println();
        } else {
            for (String colLabel : colLabelsB) {
                out.print(SET_BG_COLOR_LIGHT_GREY + colLabel + RESET_BG_COLOR);
            }
            out.println();
        }
    }

    public static void drawBoard(PrintStream out, ChessBoard board, boolean whiteAtBottom) {
        printColumnLabel(out, whiteAtBottom);
        for (int row = 0; row < BOARD_SIZE_IN_SQUARES; row++) {
            int rowLabel = whiteAtBottom ? (8 - row) : (row + 1);
            out.print(SET_BG_COLOR_LIGHT_GREY + " " + rowLabel + " " + RESET_BG_COLOR);

            for (int col = 0; col < BOARD_SIZE_IN_SQUARES; col++) {
                    int actualRow = whiteAtBottom ? 7 - row : row;
                    ChessPiece piece = board.getPiece(new ChessPosition(actualRow + 1, col + 1));
                    drawSquare(out, row, col, whiteAtBottom, piece);
            }
            out.println(SET_BG_COLOR_LIGHT_GREY + " " + rowLabel + " " + RESET_BG_COLOR);
        }
        printColumnLabel(out, whiteAtBottom);
    }

    public static void drawSquare(PrintStream out, int row, int col, boolean whiteAtBottom, ChessPiece piece) {
        boolean isLightSquare = (row + col) % 2 == 0;
        String bgColor = isLightSquare ? SET_BG_COLOR_WHITE : SET_BG_COLOR_MAGENTA;
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
