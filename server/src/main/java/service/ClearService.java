package service;

import com.google.gson.internal.LinkedTreeMap;
import dataAccess.*;
import org.eclipse.jetty.server.Authentication;

public class ClearService {
    public static void clear() throws DataAccessException {
        AuthDAO authDAO = new SQLAuthDAO();
        UserDAO userDAO = new SQLUserDAO();
        GameDAO gameDAO = new SQLGameDAO();

        authDAO.clear();
        userDAO.clear();
        gameDAO.clear();
    }
}
