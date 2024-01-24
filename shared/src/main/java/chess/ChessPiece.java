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
    public boolean isFriend(ChessPiece piece) {
        return piece != null && this.getTeamColor() == piece.getTeamColor();
    }

//    public void lineMover(ChessBoard board, ChessPosition position, HashSet<ChessMove> set, int row, int col) {
//        int newRow = position.getRow() + row;
//        int newCol = position.getColumn() + col;
//
//        ChessPosition newPosition = new ChessPosition(newRow, newCol);
//
//        while (inBounds(newPosition)) {
//
//            if (isPossiblePosition(newPosition, board)) {
//                ChessMove newMove = new ChessMove(position, newPosition, null);
//                set.add(newMove);
//
//                if(!isFriend(board.getPiece(newPosition))){
//                    break;
//                }
//            } else {
//                break;
//            }
//            newRow += row;
//            newCol += col;
//        }
//
//    }


//    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
//        HashSet<ChessMove> diagonalMoves =  new HashSet<>();
//
//            lineMover(board, myPosition, diagonalMoves, 1, 1);
//            lineMover(board, myPosition, diagonalMoves, -1, 1);
//            lineMover(board, myPosition, diagonalMoves, 1, -1);
//            lineMover(board, myPosition, diagonalMoves, -1, -1);
//
//            return diagonalMoves;
//        }

//    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
//
//    }
//    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
//
//    }
    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>();
        List<List<Integer>> directions = new ArrayList<>();

        directions.add(List.of(1,1));
        directions.add(List.of(0,1));
        directions.add(List.of(-1,1));
        directions.add(List.of(-1,0));
        directions.add(List.of(-1,-1));
        directions.add(List.of(0,-1));
        directions.add(List.of(1,-1));
        directions.add(List.of(1,0));

        for(List<Integer> direction : directions){
            int newRow = myPosition.getRow() + direction.get(0);
            int newCol = myPosition.getColumn() + direction.get(1);

            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            if (inBounds(newPosition)) {
                if(!isFriend(board.getPiece(newPosition)) || board.getPiece(newPosition) == null) {
                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    validMoves.add(move);
                }
            }
        }
        return validMoves;
    }
    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>();
        List<List<Integer>> directions = new ArrayList<>();

        directions.add(List.of(2,1));
        directions.add(List.of(1,2));
        directions.add(List.of(-2,1));
        directions.add(List.of(-1,2));
        directions.add(List.of(-2,-1));
        directions.add(List.of(-1,-2));
        directions.add(List.of(2,-1));
        directions.add(List.of(1,-2));

        for(List<Integer> direction : directions){
            int newRow = myPosition.getRow() + direction.get(0);
            int newCol = myPosition.getColumn() + direction.get(1);

            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            if (inBounds(newPosition)) {
                if(!isFriend(board.getPiece(newPosition)) || board.getPiece(newPosition) == null) {
                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    validMoves.add(move);
                }
            }
        }
        return validMoves;
    }
    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>();
        List<List<Integer>> directions = new ArrayList<>();

        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {

            directions.add(List.of(1, 0));

            if (myPosition.getRow() == 2) {
                directions.add(List.of(2, 0));
            }
//            ChessPosition topRight = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
//            if(board.getPiece(topRight).getTeamColor() == ChessGame.TeamColor.BLACK){
//                directions.add(List.of(1,1));
//            }
//            ChessPosition topLeft = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
//            if(board.getPiece(topLeft).getTeamColor() == ChessGame.TeamColor.BLACK){
//                directions.add(List.of(1,-1));
//            }
        }
        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {

            directions.add(List.of(-1, 0));

            if (myPosition.getRow() == 7) {
                directions.add(List.of(-2, 0));
            }
//            ChessPosition bottomRight = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
//            if(board.getPiece(bottomRight).getTeamColor() == ChessGame.TeamColor.WHITE){
//                directions.add(List.of(-1,1));
//            }
//            ChessPosition bottomLeft = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
//            if(board.getPiece(bottomLeft).getTeamColor() == ChessGame.TeamColor.WHITE){
//                directions.add(List.of(-1,-1));
//            }
        }
        for(List<Integer> direction : directions){
            int newRow = myPosition.getRow() + direction.get(0);
            int newCol = myPosition.getColumn() + direction.get(1);

            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            if (inBounds(newPosition)) {
                if(!isFriend(board.getPiece(newPosition)) || board.getPiece(newPosition) == null) {
//                    if(newRow == 1 || newCol == 8){
//                        ChessMove move = new ChessMove(myPosition, newPosition, null);
//                        validMoves.add(move);
//                    }
                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    validMoves.add(move);
                }

            }
        }
        return validMoves;
    }


 /*
 *  for i in possible positions
 *      if isEmpty() && isInBounds() && not isFriend() {
 *          add to arrayList
 *
 *      diagonal move function
 *
 *      if isFriend()
 *          break
 *      if not isFriend()
 *          add move
 *          break
 *      }
 *
 *  }
*/
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

//        if(board.getPiece(myPosition).getPieceType() == PieceType.BISHOP) {
//            return bishopMoves(board, myPosition);
//        }
        if(board.getPiece(myPosition).getPieceType() == PieceType.KING) {
            return kingMoves(board, myPosition);
        }
        if(board.getPiece(myPosition).getPieceType() == PieceType.KNIGHT) {
            return knightMoves(board, myPosition);
        }
        if(board.getPiece(myPosition).getPieceType() == PieceType.PAWN) {
            return pawnMoves(board, myPosition);
        }
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
