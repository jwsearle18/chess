package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor team;
    public ChessGame() {
        setTeamTurn(team);
        setBoard(board);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return team;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    private ChessPosition kingPosition(TeamColor teamColor) {
        for(int row = 1; row < 9; row++) {
            for(int col = 1; col < 9; col++) {
                ChessPiece piece = getBoard().getPiece(new ChessPosition(row, col));
                if(piece != null && piece.getPieceType().equals(ChessPiece.PieceType.KING) && piece.getTeamColor().equals(teamColor)) {
                    return new ChessPosition(row, col);
                }
            }
        }
        return null;
    }


    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = getBoard().getPiece(startPosition);
        HashSet<ChessMove> validMoves = new HashSet<>();
        if (piece == null) {
            return null;
        } else {
            for(ChessMove move : piece.pieceMoves(getBoard(), startPosition)) {
                ChessBoard newBoard = getBoard();
                newBoard
            }

        }
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = getBoard().getPiece(move.startPosition);
        if(!piece.equals(null)) {
            if(piece.getTeamColor().equals(this.team)) {
                if(validMoves(move.startPosition).contains(move)) {
                    getBoard().addPiece(move.endPosition, piece);
                    getBoard().addPiece(move.startPosition, null);
                }
            }
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */


    public boolean isInCheck(TeamColor teamColor) {
        for(int row = 1; row < 9; row++) {
            for(int col = 1; col < 9; col++) {
                ChessPiece piece = getBoard().getPiece(new ChessPosition(row,col));
                if(piece != null) {
                    if(piece.getTeamColor() != teamColor){
                        for(ChessMove move : piece.pieceMoves(getBoard(), new ChessPosition(row, col))) {
                            ChessPosition tempKing = kingPosition(teamColor);
                            if(move.endPosition.equals(tempKing)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
//        for(int row = 1; row < 9; row++) {
//            for(int col = 1; col < 9; col++) {
//                ChessPiece piece = getBoard().getPiece(new ChessPosition(row,col));
//                if(piece != null) {
//                    if(piece.getTeamColor() != teamColor){
//                        for(ChessMove move : validMoves(new ChessPosition(row, col))) {
//                            for(ChessPosition position : getEndPositions(kingPosition(board, teamColor))) {
//                                int counter = 0;
//                                if(move.endPosition == kingPosition(board, teamColor) && move.endPosition == position) {
//                                    counter += 1;
//                                    if(counter == 9){
//                                        return true;
//                                    }
//                                }
//                            }
//
//                        }
//                    }
//
//                }
//            }
//        }
//        return false;
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
