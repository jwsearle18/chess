package dataAccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{

    static final private HashMap<String, AuthData> authTokens = new HashMap<>();


    public AuthData createAuth(String username) throws DataAccessException{

        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        authTokens.put(authToken, authData);
        return authData;

    }

    public AuthData getAuth(String authToken) throws DataAccessException{

        return authTokens.get(authToken);

    }

    public void deleteAuth(String authToken) throws DataAccessException{
        authTokens.remove(authToken);
    }

    public void clear() throws DataAccessException{
        authTokens.clear();
    }
}
