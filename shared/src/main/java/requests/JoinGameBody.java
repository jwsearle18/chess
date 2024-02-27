package requests;

import chess.ChessGame;
import chess.ChessPiece;

public record JoinGameBody(ChessGame.TeamColor playerColor, int gameID) {
}
