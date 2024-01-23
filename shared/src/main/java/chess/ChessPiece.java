package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

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
        return position.getRow() < 9 && position.getColumn() < 9 && position.getRow() > 0 && position.getColumn() > 0;
    }

    public boolean isFriend(ChessPiece piece) {
        return piece != null && this.getTeamColor() == piece.getTeamColor();
    }

    public boolean isPossiblePosition(ChessPosition position, ChessBoard board) {
        return !isFriend(board.getPiece(position)) && inBounds(position);
    }



    public void mover(ChessBoard board, ChessPosition position, HashSet<ChessMove> set, int row, int col) {

        while (inBounds(position)) {
            ChessPosition newPosition = new ChessPosition(row, col);//- change the rows and cols as specified
            if (isPossiblePosition(newPosition, board)) {
                ChessMove newMove = new ChessMove(position, newPosition, null);//- in the new position, check if its a possible position.
                set.add(newMove);
                if(!isFriend(board.getPiece(newPosition))){
                    break;
                }
            } else {
                break;//- if it is, add it in the set
            }
            //          - if not friend break
            //      - else break

        }

    }

//    public ChessPosition singleDiagonalUpRight(ChessPosition position, HashSet<ChessMove> set) {
//        ChessPosition newPosition = new ChessPosition(position.getRow() + 1,position.getColumn() + 1);
//        ChessMove newMove = new ChessMove(position, newPosition, null);
//        set.add(newMove);
//        return newPosition;
//    }
//
//    public ChessPosition singleDiagonalUpLeft(ChessPosition position, HashSet<ChessMove> set) {
//        ChessPosition newPosition = new ChessPosition(position.getRow() - 1,position.getColumn() + 1);
//        ChessMove newMove = new ChessMove(position, newPosition, null);
//        set.add(newMove);
//        return newPosition;
//    }
//
//    public ChessPosition singleDiagonalDownRight(ChessPosition position, HashSet<ChessMove> set) {
//        ChessPosition newPosition = new ChessPosition(position.getRow() + 1,position.getColumn() - 1);
//        ChessMove newMove = new ChessMove(position, newPosition, null);
//        set.add(newMove);
//        return newPosition;
//    }
//
//    public ChessPosition singleDiagonalDownLeft(ChessPosition position, HashSet<ChessMove> set) {
//        ChessPosition newPosition = new ChessPosition(position.getRow() - 1,position.getColumn() - 1);
//        ChessMove newMove = new ChessMove(position, newPosition, null);
//        set.add(newMove);
//        return newPosition;
//    }
//
//    public ChessPosition singleUp(ChessPosition position, HashSet<ChessMove> set) {
//        ChessPosition newPosition = new ChessPosition(position.getRow() + 1, position.getColumn());
//        ChessMove newMove = new ChessMove(position, newPosition, null);
//        set.add(newMove);
//        return newPosition;
//    }
//
//    public ChessPosition singleDown(ChessPosition position, HashSet<ChessMove> set) {
//        ChessPosition newPosition = new ChessPosition(position.getRow() -1, position.getColumn());
//        ChessMove newMove = new ChessMove(position, newPosition, null);
//        set.add(newMove);
//        return newPosition;
//    }
//
//    public ChessPosition singleRight(ChessPosition position, HashSet<ChessMove> set) {
//        ChessPosition newPosition = new ChessPosition(position.getRow(),position.getColumn() + 1);
//        ChessMove newMove = new ChessMove(position, newPosition, null);
//        set.add(newMove);
//        return newPosition;
//    }
//
//    public ChessPosition singleLeft(ChessPosition position, HashSet<ChessMove> set) {
//        ChessPosition newPosition = new ChessPosition(position.getRow(),position.getColumn() - 1);
//        ChessMove newMove = new ChessMove(position, newPosition, null);
//        set.add(newMove);
//        return newPosition;
//    }


    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> diagonalMoves =  new HashSet<>();

        for (ChessPosition i = myPosition; isPossiblePosition(i, board); i.getRow()++) {
            mover(board, i, diagonalMoves, myPosition.getRow() + 1, myPosition.getColumn() + 1);
        }


        for (ChessPosition i = singleDiagonalUpRight(myPosition, diagonalMoves); isPossiblePosition(i, board); singleDiagonalUpRight(i, diagonalMoves)) {
                if (board.getPiece(i) != null) {
                break;
            }
        }
            return diagonalMoves;
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

        if(board.getPiece(myPosition).getPieceType() == PieceType.BISHOP) {
            return bishopMoves(board, myPosition);
        }
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
