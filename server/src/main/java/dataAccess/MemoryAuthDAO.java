package dataAccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{

    static final private HashMap<String, AuthData> authTokens = new HashMap<>();

//    public UUID createAuth(){
//        return new UUID();
//    }

    public AuthData createAuth(String username) throws DataAccessException{

        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        authTokens.put(authData.username(), authData);
        return authData;

    }

    public static void clear() throws DataAccessException{
        authTokens.clear();
    }
}
