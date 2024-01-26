package chess;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;

    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;

    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {

        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {

        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public boolean inBounds(ChessPosition position) {
        return position.getRow() >= 1 && position.getColumn() >= 1 && position.getRow() <= 8 && position.getColumn() <= 8;
    }

    public boolean isEnemy(ChessPiece piece) {
        return this.getTeamColor() != piece.getTeamColor();
    }

//    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
//
//    }
//
    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<ChessMove>();
        ArrayList<ChessPosition> possiblePositions = new ArrayList<ChessPosition>();

        possiblePositions.add(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1));
        possiblePositions.add(new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1));
        possiblePositions.add(new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()));

        for(ChessPosition position : possiblePositions) {
            if(inBounds(position)) {
                if(board.getPiece(position) == null) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                } else if(isEnemy(board.getPiece(position))) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                    continue;
                } else {
                    continue;
                }
            }
        }
        return validMoves;
    }
//
//    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
//
//    }

//    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
//        HashSet<ChessMove> validMoves = new HashSet<>();
//        List<List<Integer>> directions = new ArrayList<>();
//
//        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
//
//            directions.add(List.of(1, 0));
//
//            if (myPosition.getRow() == 2) {
//                directions.add(List.of(2, 0));
//            }
////            ChessPosition topRight = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
////            if(board.getPiece(topRight).getTeamColor() == ChessGame.TeamColor.BLACK){
////                directions.add(List.of(1,1));
////            }
////            ChessPosition topLeft = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
////            if(board.getPiece(topLeft).getTeamColor() == ChessGame.TeamColor.BLACK){
////                directions.add(List.of(1,-1));
////            }
//        }
//        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
//
//            directions.add(List.of(-1, 0));
//
//            if (myPosition.getRow() == 7) {
//                directions.add(List.of(-2, 0));
//            }
////            ChessPosition bottomRight = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
////            if(board.getPiece(bottomRight).getTeamColor() == ChessGame.TeamColor.WHITE){
////                directions.add(List.of(-1,1));
////            }
////            ChessPosition bottomLeft = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
////            if(board.getPiece(bottomLeft).getTeamColor() == ChessGame.TeamColor.WHITE){
////                directions.add(List.of(-1,-1));
////            }
//        }
//        for(List<Integer> direction : directions){
//            int newRow = myPosition.getRow() + direction.get(0);
//            int newCol = myPosition.getColumn() + direction.get(1);
//
//            ChessPosition newPosition = new ChessPosition(newRow, newCol);
//            if (inBounds(newPosition)) {
//                if(!isFriend(board.getPiece(newPosition)) || board.getPiece(newPosition) == null) {
////                    if(newRow == 1 || newCol == 8){
////                        ChessMove move = new ChessMove(myPosition, newPosition, null);
////                        validMoves.add(move);
////                    }
//                    ChessMove move = new ChessMove(myPosition, newPosition, null);
//                    validMoves.add(move);
//                }
//
//            }
//        }
//        return validMoves;
//    }

//    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
//
//    }
//
//    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
//
//    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

//        if(board.getPiece(myPosition).getPieceType() == PieceType.BISHOP) {
//            return bishopMoves(board, myPosition);
//        }
        if(board.getPiece(myPosition).getPieceType() == PieceType.KING) {
            return kingMoves(board, myPosition);
        }
//        if(board.getPiece(myPosition).getPieceType() == PieceType.KNIGHT) {
//            return knightMoves(board, myPosition);
//        }
//        if(board.getPiece(myPosition).getPieceType() == PieceType.PAWN) {
//            return pawnMoves(board, myPosition);
//        }
//        if(board.getPiece(myPosition).getPieceType() == PieceType.QUEEN) {
//            return queenMoves(board, myPosition);
//        }
//        if(board.getPiece(myPosition).getPieceType() == PieceType.ROOK) {
//            return rookMoves(board, myPosition);
//        }
        return new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}
