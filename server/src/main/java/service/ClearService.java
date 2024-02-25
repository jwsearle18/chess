package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;

public class ClearService {
    public static void clear() throws DataAccessException {
        MemoryAuthDAO.clear();
        MemoryUserDAO.clear();
        MemoryGameDAO.clear();
    }
}
