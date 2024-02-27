package requests;

import chess.ChessGame;
import chess.ChessPiece;

public record JoinGameRequest(String authToken, ChessGame.TeamColor playerColor, int gameID) {
}
