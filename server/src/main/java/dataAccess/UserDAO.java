package dataAccess;

import model.UserData;

public interface UserDAO {
    void createUser(UserData userData) throws DataAccessException;
    UserData getUser(String userData) throws DataAccessException;

    void clear() throws DataAccessException;
}
