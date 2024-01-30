package chess;

import java.lang.reflect.Array;
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
        return piece != null && this.getTeamColor() != piece.getTeamColor();
    }

    Collection<ChessPosition> upRight(ChessPosition myPosition) {
        ArrayList<ChessPosition> possiblePositions = new ArrayList<ChessPosition>();
        possiblePositions.add(new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()+1));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+2,myPosition.getColumn()+2));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+3,myPosition.getColumn()+3));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+4,myPosition.getColumn()+4));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+5,myPosition.getColumn()+5));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+6,myPosition.getColumn()+6));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+7,myPosition.getColumn()+7));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+8,myPosition.getColumn()+8));
        return possiblePositions;
    }
    Collection<ChessPosition> downRight(ChessPosition myPosition) {
        ArrayList<ChessPosition> possiblePositions = new ArrayList<ChessPosition>();
        possiblePositions.add(new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()+1));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-2,myPosition.getColumn()+2));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-3,myPosition.getColumn()+3));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-4,myPosition.getColumn()+4));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-5,myPosition.getColumn()+5));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-6,myPosition.getColumn()+6));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-7,myPosition.getColumn()+7));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-8,myPosition.getColumn()+8));
        return possiblePositions;
    }
    Collection<ChessPosition> downLeft(ChessPosition myPosition) {
        ArrayList<ChessPosition> possiblePositions = new ArrayList<ChessPosition>();
        possiblePositions.add(new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()-1));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-2,myPosition.getColumn()-2));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-3,myPosition.getColumn()-3));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-4,myPosition.getColumn()-4));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-5,myPosition.getColumn()-5));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-6,myPosition.getColumn()-6));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-7,myPosition.getColumn()-7));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-8,myPosition.getColumn()-8));
        return possiblePositions;
    }
    Collection<ChessPosition> upLeft(ChessPosition myPosition) {
        ArrayList<ChessPosition> possiblePositions = new ArrayList<ChessPosition>();
        possiblePositions.add(new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()-1));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+2,myPosition.getColumn()-2));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+3,myPosition.getColumn()-3));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+4,myPosition.getColumn()-4));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+5,myPosition.getColumn()-5));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+6,myPosition.getColumn()-6));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+7,myPosition.getColumn()-7));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+8,myPosition.getColumn()-8));
        return possiblePositions;
    }
    Collection<ChessPosition> right(ChessPosition myPosition) {
        ArrayList<ChessPosition> possiblePositions = new ArrayList<ChessPosition>();
        possiblePositions.add(new ChessPosition(myPosition.getRow(),myPosition.getColumn()+1));
        possiblePositions.add(new ChessPosition(myPosition.getRow(),myPosition.getColumn()+2));
        possiblePositions.add(new ChessPosition(myPosition.getRow(),myPosition.getColumn()+3));
        possiblePositions.add(new ChessPosition(myPosition.getRow(),myPosition.getColumn()+4));
        possiblePositions.add(new ChessPosition(myPosition.getRow(),myPosition.getColumn()+5));
        possiblePositions.add(new ChessPosition(myPosition.getRow(),myPosition.getColumn()+6));
        possiblePositions.add(new ChessPosition(myPosition.getRow(),myPosition.getColumn()+7));
        possiblePositions.add(new ChessPosition(myPosition.getRow(),myPosition.getColumn()+8));
        return possiblePositions;
    }
    Collection<ChessPosition> down(ChessPosition myPosition) {
        ArrayList<ChessPosition> possiblePositions = new ArrayList<ChessPosition>();
        possiblePositions.add(new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-2,myPosition.getColumn()));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-3,myPosition.getColumn()));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-4,myPosition.getColumn()));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-5,myPosition.getColumn()));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-6,myPosition.getColumn()));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-7,myPosition.getColumn()));
        possiblePositions.add(new ChessPosition(myPosition.getRow()-8,myPosition.getColumn()));
        return possiblePositions;
    }
    Collection<ChessPosition> left(ChessPosition myPosition) {
        ArrayList<ChessPosition> possiblePositions = new ArrayList<ChessPosition>();
        possiblePositions.add(new ChessPosition(myPosition.getRow(),myPosition.getColumn()-1));
        possiblePositions.add(new ChessPosition(myPosition.getRow(),myPosition.getColumn()-2));
        possiblePositions.add(new ChessPosition(myPosition.getRow(),myPosition.getColumn()-3));
        possiblePositions.add(new ChessPosition(myPosition.getRow(),myPosition.getColumn()-4));
        possiblePositions.add(new ChessPosition(myPosition.getRow(),myPosition.getColumn()-5));
        possiblePositions.add(new ChessPosition(myPosition.getRow(),myPosition.getColumn()-6));
        possiblePositions.add(new ChessPosition(myPosition.getRow(),myPosition.getColumn()-7));
        possiblePositions.add(new ChessPosition(myPosition.getRow(),myPosition.getColumn()-8));
        return possiblePositions;
    }
    Collection<ChessPosition> up(ChessPosition myPosition) {
        ArrayList<ChessPosition> possiblePositions = new ArrayList<ChessPosition>();
        possiblePositions.add(new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+2,myPosition.getColumn()));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+3,myPosition.getColumn()));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+4,myPosition.getColumn()));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+5,myPosition.getColumn()));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+6,myPosition.getColumn()));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+7,myPosition.getColumn()));
        possiblePositions.add(new ChessPosition(myPosition.getRow()+8,myPosition.getColumn()));
        return possiblePositions;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>();
        ArrayList<ChessPosition> possiblePositions1 = new ArrayList<>(upRight(myPosition));
        ArrayList<ChessPosition> possiblePositions2 = new ArrayList<>(downRight(myPosition));
        ArrayList<ChessPosition> possiblePositions3 = new ArrayList<>(downLeft(myPosition));
        ArrayList<ChessPosition> possiblePositions4 = new ArrayList<>(upLeft(myPosition));

        for(ChessPosition position : possiblePositions1) {
            if(inBounds(position)) {
                if(board.getPiece(position) == null) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                } else if(isEnemy(board.getPiece(position))) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                    break;
                } else {
                    break;
                }
            }
        }
        for(ChessPosition position : possiblePositions2) {
            if(inBounds(position)) {
                if(board.getPiece(position) == null) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                } else if(isEnemy(board.getPiece(position))) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                    break;
                } else {
                    break;
                }
            }
        }
        for(ChessPosition position : possiblePositions3) {
            if(inBounds(position)) {
                if(board.getPiece(position) == null) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                } else if(isEnemy(board.getPiece(position))) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                    break;
                } else {
                    break;
                }
            }
        }
        for(ChessPosition position : possiblePositions4) {
            if(inBounds(position)) {
                if(board.getPiece(position) == null) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                } else if(isEnemy(board.getPiece(position))) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                    break;
                } else {
                    break;
                }
            }
        }
        return validMoves;
    }

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

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<ChessMove>();
        ArrayList<ChessPosition> possiblePositions = new ArrayList<ChessPosition>();

        possiblePositions.add(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1));
        possiblePositions.add(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2));
        possiblePositions.add(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2));
        possiblePositions.add(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1));
        possiblePositions.add(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1));
        possiblePositions.add(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2));
        possiblePositions.add(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2));
        possiblePositions.add(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1));

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

    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>();
        ArrayList<ChessPosition> possiblePositions1 = new ArrayList<>(up(myPosition));
        ArrayList<ChessPosition> possiblePositions2 = new ArrayList<>(down(myPosition));
        ArrayList<ChessPosition> possiblePositions3 = new ArrayList<>(left(myPosition));
        ArrayList<ChessPosition> possiblePositions4 = new ArrayList<>(right(myPosition));
        ArrayList<ChessPosition> possiblePositions5 = new ArrayList<>(upRight(myPosition));
        ArrayList<ChessPosition> possiblePositions6 = new ArrayList<>(downRight(myPosition));
        ArrayList<ChessPosition> possiblePositions7 = new ArrayList<>(downLeft(myPosition));
        ArrayList<ChessPosition> possiblePositions8 = new ArrayList<>(upLeft(myPosition));

        for(ChessPosition position : possiblePositions1) {
            if(inBounds(position)) {
                if(board.getPiece(position) == null) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                } else if(isEnemy(board.getPiece(position))) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                    break;
                } else {
                    break;
                }
            }
        }
        for(ChessPosition position : possiblePositions2) {
            if(inBounds(position)) {
                if(board.getPiece(position) == null) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                } else if(isEnemy(board.getPiece(position))) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                    break;
                } else {
                    break;
                }
            }
        }
        for(ChessPosition position : possiblePositions3) {
            if(inBounds(position)) {
                if(board.getPiece(position) == null) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                } else if(isEnemy(board.getPiece(position))) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                    break;
                } else {
                    break;
                }
            }
        }
        for(ChessPosition position : possiblePositions4) {
            if(inBounds(position)) {
                if(board.getPiece(position) == null) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                } else if(isEnemy(board.getPiece(position))) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                    break;
                } else {
                    break;
                }
            }
        }
        for(ChessPosition position : possiblePositions5) {
            if(inBounds(position)) {
                if(board.getPiece(position) == null) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                } else if(isEnemy(board.getPiece(position))) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                    break;
                } else {
                    break;
                }
            }
        }
        for(ChessPosition position : possiblePositions6) {
            if(inBounds(position)) {
                if(board.getPiece(position) == null) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                } else if(isEnemy(board.getPiece(position))) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                    break;
                } else {
                    break;
                }
            }
        }
        for(ChessPosition position : possiblePositions7) {
            if(inBounds(position)) {
                if(board.getPiece(position) == null) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                } else if(isEnemy(board.getPiece(position))) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                    break;
                } else {
                    break;
                }
            }
        }
        for(ChessPosition position : possiblePositions8) {
            if(inBounds(position)) {
                if(board.getPiece(position) == null) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                } else if(isEnemy(board.getPiece(position))) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                    break;
                } else {
                    break;
                }
            }
        }
        return validMoves;
    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>();
        ArrayList<ChessPosition> possiblePositions1 = new ArrayList<>(up(myPosition));
        ArrayList<ChessPosition> possiblePositions2 = new ArrayList<>(down(myPosition));
        ArrayList<ChessPosition> possiblePositions3 = new ArrayList<>(left(myPosition));
        ArrayList<ChessPosition> possiblePositions4 = new ArrayList<>(right(myPosition));

        for(ChessPosition position : possiblePositions1) {
            if(inBounds(position)) {
                if(board.getPiece(position) == null) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                } else if(isEnemy(board.getPiece(position))) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                    break;
                } else {
                    break;
                }
            }
        }
        for(ChessPosition position : possiblePositions2) {
            if(inBounds(position)) {
                if(board.getPiece(position) == null) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                } else if(isEnemy(board.getPiece(position))) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                    break;
                } else {
                    break;
                }
            }
        }
        for(ChessPosition position : possiblePositions3) {
            if(inBounds(position)) {
                if(board.getPiece(position) == null) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                } else if(isEnemy(board.getPiece(position))) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                    break;
                } else {
                    break;
                }
            }
        }
        for(ChessPosition position : possiblePositions4) {
            if(inBounds(position)) {
                if(board.getPiece(position) == null) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                } else if(isEnemy(board.getPiece(position))) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                    break;
                } else {
                    break;
                }
            }
        }
        return validMoves;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<ChessMove>();
        ArrayList<ChessPosition> possiblePositions = new ArrayList<ChessPosition>();

        if (this.getTeamColor() == ChessGame.TeamColor.WHITE) {
            possiblePositions.add(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()));
            if (myPosition.getRow() == 2 && board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn())) == null) {
                possiblePositions.add(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn()));
            }
            ChessPosition topRightPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            if (isEnemy(board.getPiece(topRightPosition))) {
                if(topRightPosition.getRow() == 1) {
                    validMoves.add(new ChessMove(myPosition, topRightPosition, PieceType.ROOK));
                    validMoves.add(new ChessMove(myPosition, topRightPosition, PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, topRightPosition, PieceType.BISHOP));
                    validMoves.add(new ChessMove(myPosition, topRightPosition, PieceType.KNIGHT));
                } else {
                    validMoves.add(new ChessMove(myPosition, topRightPosition, null));
                }
            }
            ChessPosition topLeftPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            if (isEnemy(board.getPiece(topLeftPosition))) {
                if(topLeftPosition.getRow() == 1) {
                    validMoves.add(new ChessMove(myPosition, topLeftPosition, PieceType.ROOK));
                    validMoves.add(new ChessMove(myPosition, topLeftPosition, PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, topLeftPosition, PieceType.BISHOP));
                    validMoves.add(new ChessMove(myPosition, topLeftPosition, PieceType.KNIGHT));
                } else {
                    validMoves.add(new ChessMove(myPosition, topLeftPosition, null));
                }
            }
            for (ChessPosition position : possiblePositions) {
                if (inBounds(position)) {
                    if (board.getPiece(position) == null) {
                        if(position.getRow() == 8) {
                            validMoves.add(new ChessMove(myPosition, position, PieceType.ROOK));
                            validMoves.add(new ChessMove(myPosition, position, PieceType.QUEEN));
                            validMoves.add(new ChessMove(myPosition, position, PieceType.BISHOP));
                            validMoves.add(new ChessMove(myPosition, position, PieceType.KNIGHT));
                        } else {
                            validMoves.add(new ChessMove(myPosition, position, null));
                        }
                    } else {
                        continue;
                    }
                }
            }
        }

        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
            possiblePositions.add(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()));
            if (myPosition.getRow() == 7 && board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn())) == null) {
                possiblePositions.add(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn()));
            }
            ChessPosition bottomRightPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
            if (isEnemy(board.getPiece(bottomRightPosition))) {
                if(bottomRightPosition.getRow() == 1) {
                    validMoves.add(new ChessMove(myPosition, bottomRightPosition, PieceType.ROOK));
                    validMoves.add(new ChessMove(myPosition, bottomRightPosition, PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, bottomRightPosition, PieceType.BISHOP));
                    validMoves.add(new ChessMove(myPosition, bottomRightPosition, PieceType.KNIGHT));
                } else {
                    validMoves.add(new ChessMove(myPosition, bottomRightPosition, null));
                }
            }
            ChessPosition bottomLeftPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
            if (isEnemy(board.getPiece(bottomLeftPosition))) {
                if(bottomLeftPosition.getRow() == 1) {
                    validMoves.add(new ChessMove(myPosition, bottomLeftPosition, PieceType.ROOK));
                    validMoves.add(new ChessMove(myPosition, bottomLeftPosition, PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, bottomLeftPosition, PieceType.BISHOP));
                    validMoves.add(new ChessMove(myPosition, bottomLeftPosition, PieceType.KNIGHT));
                } else {
                    validMoves.add(new ChessMove(myPosition, bottomLeftPosition, null));
                }
            }
            for (ChessPosition position : possiblePositions) {
                if (inBounds(position)) {
                    if (board.getPiece(position) == null) {
                        if(position.getRow() == 1) {
                            validMoves.add(new ChessMove(myPosition, position, PieceType.ROOK));
                            validMoves.add(new ChessMove(myPosition, position, PieceType.QUEEN));
                            validMoves.add(new ChessMove(myPosition, position, PieceType.BISHOP));
                            validMoves.add(new ChessMove(myPosition, position, PieceType.KNIGHT));
                        } else {
                            validMoves.add(new ChessMove(myPosition, position, null));
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        return validMoves;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        if(board.getPiece(myPosition).getPieceType() == PieceType.BISHOP) {
            return bishopMoves(board, myPosition);
        }
        if(board.getPiece(myPosition).getPieceType() == PieceType.KING) {
            return kingMoves(board, myPosition);
        }
        if(board.getPiece(myPosition).getPieceType() == PieceType.KNIGHT) {
            return knightMoves(board, myPosition);
        }
        if(board.getPiece(myPosition).getPieceType() == PieceType.PAWN) {
            return pawnMoves(board, myPosition);
        }
        if(board.getPiece(myPosition).getPieceType() == PieceType.QUEEN) {
            return queenMoves(board, myPosition);
        }
        if(board.getPiece(myPosition).getPieceType() == PieceType.ROOK) {
            return rookMoves(board, myPosition);
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
