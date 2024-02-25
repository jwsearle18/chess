package dataAccess;

import model.AuthData;

import java.util.UUID;

public interface AuthDAO {
    public AuthData createAuth(String username) throws DataAccessException;
}
