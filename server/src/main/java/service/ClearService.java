package service;

import com.google.gson.internal.LinkedTreeMap;
import dataAccess.*;
import org.eclipse.jetty.server.Authentication;

public class ClearService {
    public static void clear() throws DataAccessException {
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        authDAO.clear();
        userDAO.clear();
        gameDAO.clear();
    }
}
