package chess;

import java.util.ArrayList;
import java.util.Collection;
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
        return this.getTeamColor() == piece.getTeamColor();
    }

//    public square isPossiblePosition(ChessPosition position) {
//        ChessPiece pieceAtPosition =
//        if(not isFriend(position) && inBounds(position)){
//            return position;
//        }
//    }
//
//
//    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
//
//
//        Collection possiblePositions; []
//   square calculatePossiblePosition(ChessPosition myPosition){
//            while (inBounds(myPosition)) {
//                myPosition[myPosition.getColumn() + 1] && row + 1;
//                i < length
//            }
//            create new arrayList;
//        }

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
            bishopMoves(board, myPosition);
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
