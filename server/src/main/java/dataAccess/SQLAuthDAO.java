package dataAccess;

import model.AuthData;
import service.RegisterService;

import java.sql.*;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{

    public SQLAuthDAO() throws DataAccessException {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() throws DataAccessException {
        String sql = """
                CREATE TABLE IF NOT EXISTS auths (
                    authToken VARCHAR(255) PRIMARY KEY,
                    username VARCHAR(255) NOT NULL
                )
                """;
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             Statement statement = conn.createStatement()) {
            statement.executeUpdate("TRUNCATE auths");
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO auths (authToken, username) VALUES (?, ?)")) {
            preparedStatement.setString(1, authToken);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();

            return new AuthData(authToken, username);

        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT username FROM auths WHERE authToken = ?")) {
            preparedStatement.setString(1, authToken);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                String username = rs.getString("username");
                return new AuthData(authToken, username);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM auths WHERE authToken = ?")) {
            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
 }
