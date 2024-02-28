package chess;

import java.util.*;

/**
 * Represents a single chess piece in a chess game.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    public enum PieceType {
        KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN
    }

    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    public PieceType getPieceType() {
        return type;
    }

    private boolean inBounds(ChessPosition position) {
        return position.getRow() >= 1 && position.getColumn() >= 1 && position.getRow() <= 8 && position.getColumn() <= 8;
    }

    private boolean isEnemy(ChessPiece piece) {
        return piece != null && this.pieceColor != piece.pieceColor;
    }

    private Collection<ChessPosition> generateLineMoves(ChessPosition position, int rowIncrement, int colIncrement) {
        Collection<ChessPosition> positions = new ArrayList<>();
        ChessPosition newPos = new ChessPosition(position.getRow() + rowIncrement, position.getColumn() + colIncrement);
        while (inBounds(newPos)) {
            positions.add(newPos);
            newPos = new ChessPosition(newPos.getRow() + rowIncrement, newPos.getColumn() + colIncrement);
        }
        return positions;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return switch (this.type) {
            case BISHOP -> generateDiagonalMoves(board, myPosition);
            case KING -> generateKingMoves(board, myPosition);
            case KNIGHT -> generateKnightMoves(board, myPosition);
            case PAWN -> generatePawnMoves(board, myPosition);
            case QUEEN -> generateQueenMoves(board, myPosition);
            case ROOK -> generateRookMoves(board, myPosition);
            default -> Collections.emptyList();
        };
    }

    // Diagonal Moves for Bishop and Queen
    private Collection<ChessMove> generateDiagonalMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>();
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        for (int[] dir : directions) {
            for (ChessPosition position : generateLineMoves(myPosition, dir[0], dir[1])) {
                if (board.getPiece(position) == null) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                } else {
                    if (isEnemy(board.getPiece(position))) {
                        validMoves.add(new ChessMove(myPosition, position, null));
                    }
                    break; // Stop at the first piece encountered
                }
            }
        }
        return validMoves;
    }

    // Straight Moves for Rook and Queen
    private Collection<ChessMove> generateStraightMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>();
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        for (int[] dir : directions) {
            for (ChessPosition position : generateLineMoves(myPosition, dir[0], dir[1])) {
                if (board.getPiece(position) == null) {
                    validMoves.add(new ChessMove(myPosition, position, null));
                } else {
                    if (isEnemy(board.getPiece(position))) {
                        validMoves.add(new ChessMove(myPosition, position, null));
                    }
                    break; // Stop at the first piece encountered
                }
            }
        }
        return validMoves;
    }

    // Combined Moves for Queen
    private Collection<ChessMove> generateQueenMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>(generateDiagonalMoves(board, myPosition));
        validMoves.addAll(generateStraightMoves(board, myPosition));
        return validMoves;
    }

    // King Moves
    private Collection<ChessMove> generateKingMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>();
        int[][] directions = {{1, 1}, {1, 0}, {1, -1}, {0, 1}, {0, -1}, {-1, 1}, {-1, 0}, {-1, -1}};

        for (int[] dir : directions) {
            ChessPosition newPos = new ChessPosition(myPosition.getRow() + dir[0], myPosition.getColumn() + dir[1]);
            if (inBounds(newPos)) {
                ChessPiece piece = board.getPiece(newPos);
                if (piece == null || isEnemy(piece)) {
                    validMoves.add(new ChessMove(myPosition, newPos, null));
                }
            }
        }
        return validMoves;
    }

    // Knight Moves
    private Collection<ChessMove> generateKnightMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>();
        int[][] moves = {{2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -2}, {2, -1}};

        for (int[] move : moves) {
            ChessPosition newPos = new ChessPosition(myPosition.getRow() + move[0], myPosition.getColumn() + move[1]);
            if (inBounds(newPos)) {
                ChessPiece piece = board.getPiece(newPos);
                if (piece == null || isEnemy(piece)) {
                    validMoves.add(new ChessMove(myPosition, newPos, null));
                }
            }
        }
        return validMoves;
    }

    // Pawn Moves
    private Collection<ChessMove> generatePawnMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>();
        int direction = pieceColor == ChessGame.TeamColor.WHITE ? 1 : -1;
        int startRow = pieceColor == ChessGame.TeamColor.WHITE ? 2 : 7;
        int promotionRow = pieceColor == ChessGame.TeamColor.WHITE ? 8 : 1;

        // Forward moves
        ChessPosition oneStep = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn());
        if (inBounds(oneStep) && board.getPiece(oneStep) == null) {
            if (oneStep.getRow() == promotionRow) {
                addPromotionMoves(myPosition, oneStep, validMoves);
            } else {
                validMoves.add(new ChessMove(myPosition, oneStep, null));
            }

            // Double step from start
            if (myPosition.getRow() == startRow) {
                ChessPosition twoSteps = new ChessPosition(myPosition.getRow() + 2 * direction, myPosition.getColumn());
                if (board.getPiece(twoSteps) == null) {
                    validMoves.add(new ChessMove(myPosition, twoSteps, null));
                }
            }
        }

        // Captures
        int[] captureColumns = {myPosition.getColumn() - 1, myPosition.getColumn() + 1};
        for (int col : captureColumns) {
            ChessPosition capturePos = new ChessPosition(myPosition.getRow() + direction, col);
            if (inBounds(capturePos)) {
                ChessPiece piece = board.getPiece(capturePos);
                if (piece != null && isEnemy(piece)) {
                    if (capturePos.getRow() == promotionRow) {
                        addPromotionMoves(myPosition, capturePos, validMoves);
                    } else {
                        validMoves.add(new ChessMove(myPosition, capturePos, null));
                    }
                }
            }
        }

        return validMoves;
    }

    private void addPromotionMoves(ChessPosition from, ChessPosition to, Set<ChessMove> moves) {
        for (PieceType type : new PieceType[]{PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT}) {
            moves.add(new ChessMove(from, to, type));
        }
    }

    // Rook Moves
    private Collection<ChessMove> generateRookMoves(ChessBoard board, ChessPosition myPosition) {
        return generateStraightMoves(board, myPosition);
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
        return "ChessPiece{" + "pieceColor=" + pieceColor + ", type=" + type + '}';
    }
}
