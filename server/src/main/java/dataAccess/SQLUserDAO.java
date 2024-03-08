package dataAccess;

import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static dataAccess.DatabaseManager.getConnection;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        configureDatabase();
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                username VARCHAR(255) PRIMARY KEY,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL
                )
                """;
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             Statement statement = conn.createStatement()) {
            statement.executeUpdate("TRUNCATE users");
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(userData.password());

        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO users (username, password, email) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, userData.username());
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, userData.email());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT username, password, email FROM users WHERE username=?")) {
            preparedStatement.setString(1, username);
            var rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }
}
