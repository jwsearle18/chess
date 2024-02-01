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

    private ChessPosition kingPosition(ChessBoard board, TeamColor teamColor) {
        for(int row = 1; row < 9; row++) {
            for(int col = 1; col < 9; col++) {
                ChessPiece piece = board[row][col];
                if(piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
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
        if (piece == null) {
            return null;
        } else {
            return piece.pieceMoves(getBoard(), startPosition);
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
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
                ChessPiece piece = board[row][col];
                if(piece != null) {
                    if(piece.getTeamColor() != teamColor){
                        for(ChessMove move : validMoves(new ChessPosition(row, col))) {
                            if(move.endPosition == kingPosition(board, teamColor)) {
                                return true;
                            }
                        }
                    }

                }
            }
        }
        return false;
    }

    private Collection<ChessPosition> getEndPositions(ChessPosition startPosition) {
        HashSet<ChessPosition> endPositions = new HashSet<ChessPosition>();
        for(ChessMove move : validMoves(startPosition) {
            endPositions.add(move.endPosition);
        }
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        for(int row = 1; row < 9; row++) {
            for(int col = 1; col < 9; col++) {
                ChessPiece piece = board[row][col];
                if(piece != null) {
                    if(piece.getTeamColor() != teamColor){
                        for(ChessMove move : validMoves(new ChessPosition(row, col))) {
                            for(ChessPosition position : getEndPositions(kingPosition(board, teamColor))) {
                                int counter =0;
                                if(move.endPosition == kingPosition(board, teamColor) && move.endPosition == position) {
                                    counter += 1;
                                    if(counter == 6){
                                        return true;
                                    }
                                }
                            }

                        }
                    }

                }
            }
        }
        return false;
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
