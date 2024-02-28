package chess;

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
        setTeamTurn(TeamColor.WHITE);
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

    private ChessPosition kingPosition(TeamColor teamColor, ChessBoard board) {
        for(int row = 1; row < 9; row++) {
            for(int col = 1; col < 9; col++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if(piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    return new ChessPosition(row, col);
                }
            }
        }
        return null;
    }

    public ChessBoard copyBoard(ChessBoard board) {
        ChessPiece[][] copiedBoard = new ChessPiece[8][8];
        for(int row = 1; row < 9; row++) {
            for(int col = 1; col < 9; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                copiedBoard[row-1][col-1] = piece;
            }
        }
        return new ChessBoard(copiedBoard);
    }



    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
        public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        HashSet<ChessMove> validMoves = new HashSet<>();
            if (piece != null) {

                for (ChessMove move : piece.pieceMoves(board, startPosition)) {

                    ChessBoard difChessBoard = copyBoard(board);
                    difChessBoard.addPiece(move.endPosition, piece);
                    difChessBoard.addPiece(move.startPosition, null);
                    if (!copyBoardInCheck(piece.getTeamColor(), difChessBoard)) {
                        validMoves.add(move);
                    }
                }

           }
        return validMoves;
        }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.startPosition);
        if (piece == null) {
            throw new InvalidMoveException("Invalid Move");
        } else {
            if (piece.getTeamColor() != team) {
                throw new InvalidMoveException("Invalid Move");
            } else {
                if (!validMoves(move.startPosition).contains(move)) {
                    throw new InvalidMoveException("Invalid Move");
                } else {
                    if (move.promotionPiece != null) {
                        board.addPiece(move.endPosition, new ChessPiece(piece.getTeamColor(), move.promotionPiece));
                        board.addPiece(move.startPosition, null);
                    } else {
                        board.addPiece(move.endPosition, piece);
                        board.addPiece(move.startPosition, null);
                    }
                    team = (getTeamTurn() == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
                }
            }
        }
    }
        // if piece is null
        // if its not the turn of piece trying to move
        // if move trying to make is not returned by valid moves


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */

    public boolean copyBoardInCheck(TeamColor teamColor, ChessBoard copiedBoard) {
        for(int row = 1; row < 9; row++) {
            for(int col = 1; col < 9; col++) {
                ChessPiece piece = copiedBoard.getPiece(new ChessPosition(row,col));
                if(piece != null) {
                    if(piece.getTeamColor() != teamColor){
                        for(ChessMove move : piece.pieceMoves(copiedBoard, new ChessPosition(row, col))) {
                            ChessPosition tempKing = kingPosition(teamColor, copiedBoard);
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


    public boolean isInCheck(TeamColor teamColor) {
        for(int row = 1; row < 9; row++) {
            for(int col = 1; col < 9; col++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row,col));
                if(piece != null) {
                    if(piece.getTeamColor() != teamColor){
                        for(ChessMove move : piece.pieceMoves(board, new ChessPosition(row, col))) {
                            ChessPosition tempKingPos = kingPosition(teamColor, board);
                            if(move.endPosition.equals(tempKingPos)) {
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
        if(isInCheck(teamColor)) {
            for (int row = 1; row < 9; row++) {
                for (int col = 1; col < 9; col++) {
                    ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                    if (piece != null) {
                        if (piece.getTeamColor() == teamColor) {
                            for (ChessMove move : validMoves(new ChessPosition(row, col))) {
                                ChessBoard copiedBoard = copyBoard(board);
                                copiedBoard.addPiece(move.endPosition, piece);
                                copiedBoard.addPiece(move.startPosition, null);
                                if (!copyBoardInCheck(teamColor, copiedBoard)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            return true;
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
        if(!isInCheck(teamColor)) {

            for (int row = 1; row < 9; row++) {
                for (int col = 1; col < 9; col++) {
                    ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                    if (piece != null && piece.getTeamColor() == teamColor) {
                        for (ChessMove move : validMoves(new ChessPosition(row, col))) {
                            ChessBoard copiedBoard = copyBoard(board);
                            copiedBoard.addPiece(move.endPosition, piece);
                            copiedBoard.addPiece(move.startPosition, null);
                            if (!copyBoardInCheck(teamColor, copiedBoard)) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
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




