package dataAccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    static final private HashMap<String, UserData> users = new HashMap<>();

    public void clear() throws DataAccessException{
        users.clear();
    }

    public void createUser(UserData user) {
        users.put(user.username(), user);
    }

    public UserData getUser(String username) {

        return users.get(username);

    }

}
