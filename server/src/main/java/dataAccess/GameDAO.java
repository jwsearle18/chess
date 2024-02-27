package dataAccess;

import results.CreateGameResult;

public interface GameDAO {

    public CreateGameResult createGame() throws DataAccessException;
    public void clear() throws DataAccessException;
}
