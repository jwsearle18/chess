package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.GameData;
import requests.Game;

import java.sql.*;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO{

    public SQLGameDAO() throws DataAccessException {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() throws DataAccessException {
        String sql = """
            CREATE TABLE IF NOT EXISTS games (
                gameID INT PRIMARY KEY,
                whiteUsername VARCHAR(255),
                blackUsername VARCHAR(255),
                gameName VARCHAR(255),
                chessGame longtext NOT NULL
                """;
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private final Gson gson = new GsonBuilder().create();

    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             Statement statement = conn.createStatement()) {
            statement.executeUpdate("TRUNCATE games");
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        String chessGameJson = gson.toJson(gameData.game());
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, chessGame) VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, gameData.gameID());
            preparedStatement.setString(2, gameData.whiteUsername());
            preparedStatement.setString(3, gameData.blackUsername());
            preparedStatement.setString(4, gameData.gameName());
            preparedStatement.setString(5, chessGameJson);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM games WHERE gameID = ?")) {
            preparedStatement.setInt(1, gameID);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                String chessGameJson = rs.getString("chessGame");

                ChessGame currentGame = gson.fromJson(chessGameJson, ChessGame.class);
                return new GameData(rs.getInt("gameID"), rs.getString("whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"), currentGame);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public ArrayList<Game> listGames() throws DataAccessException {
        ArrayList<Game> gameList = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName FROM games")) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Game game = new Game(rs.getInt("gameID"), rs.getString("whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"));
                gameList.add(game);
            }

        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return gameList;
    }

    @Override
    public void updateGame(GameData newGameData) throws DataAccessException {
        String chessGameJson = gson.toJson(newGameData.game());

        try(Connection conn = DatabaseManager.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("UPDATE games SET whiteUsername = ?, blackUsername = ?, gameName = ?, chessGame = ? WHERE gameID = ?")) {
            preparedStatement.setString(1, newGameData.whiteUsername());
            preparedStatement.setString(2, newGameData.blackUsername());
            preparedStatement.setString(3, newGameData.gameName());
            preparedStatement.setString(4, chessGameJson);
            preparedStatement.setInt(5, newGameData.gameID());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
