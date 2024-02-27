package server;

import Failures.F400;
import Failures.F401;
import Failures.F403;
import com.google.gson.Gson;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import requests.*;
import results.CreateGameResult;
import results.ListGamesResult;
import results.LoginResult;
import results.RegisterResult;
import service.*;
import spark.*;

import javax.xml.crypto.Data;
import java.nio.file.Paths;

public class Server {


    private Object listNames(Request req, Response res) {
        res.type("application/json");
        return "{}";
    }

    private Object clear(Request req, Response res) {

        try {
            ClearService.clear();
            res.status(200);
            return "{}";
        }

        catch(DataAccessException e) {
            res.status(500);
            return "{ \"message\": \"Error: description\" }";
        }
    }
//    private Object logout(Request req, Response res) {
//        String authToken = req.headers("Authorization");
//    }
    private Object register(Request req, Response res) {
//        System.out.println(req.body());
        RegisterService registerService = new RegisterService();
        Gson gson = new Gson();
        UserData userData = gson.fromJson(req.body(), UserData.class);
        try {
            res.status(200);
            AuthData result = registerService.register(userData);
            return gson.toJson(result);
        } catch (DataAccessException e){
            res.status(500);
            return "{ \"message\": \"Error: description\" }";
        } catch (F400 f){
            res.status(400);
            return "{ \"message\": \"Error: bad request\" }";
        } catch (F403 f) {
            res.status(403);
            return "{ \"message\": \"Error: already taken\" }";
        }
    }

    private Object login(Request req, Response res) {
        LoginService loginService = new LoginService();
        Gson gson = new Gson();
        LoginRequest loginRequest = gson.fromJson(req.body(), LoginRequest.class);
        try {
            res.status(200);
            AuthData authDataResult = loginService.login(loginRequest);
            LoginResult loginResult = new LoginResult(authDataResult.username(), authDataResult.authToken());
            return gson.toJson(loginResult);
        } catch (DataAccessException e) {
            res.status(500);
            return "{ \"message\": \"Error: description\" }";
        } catch (F401 f) {
            res.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        }
    }

    private Object logout(Request req, Response res) {
        LogoutService logoutService = new LogoutService();
        LogoutRequest logoutRequest = new LogoutRequest(req.headers("authorization"));
        try {
            res.status(200);
            logoutService.logout(logoutRequest);
            return "{}";
        } catch (DataAccessException e) {
            res.status(500);
            return "{ \"message\": \"Error: description\" }";
        } catch (F401 f) {
            res.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        }
    }

    private Object listGames(Request req, Response res) {
        ListGamesService listGamesService = new ListGamesService();
        ListGamesRequest listGamesRequest = new ListGamesRequest(req.headers("authorization"));
        Gson gson = new Gson();
        try {
            res.status(200);
            ListGamesResult listGamesResult = listGamesService.listGames(listGamesRequest);
            return gson.toJson(listGamesResult);
        } catch (DataAccessException e) {
            res.status(500);
            return "{ \"message\": \"Error: description\" }";
        } catch (F401 f) {
            res.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        }
    }

    private Object createGame(Request req, Response res) {
        CreateGameService createGameService = new CreateGameService();
        Gson gson = new Gson();
        String authToken = req.headers("authorization");
        GameName gameName = gson.fromJson(req.body(), GameName.class);
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, gameName.gameName());
        try {
            res.status(200);
            CreateGameResult createGameResult = CreateGameService.createGame(createGameRequest);
            return gson.toJson(createGameResult);
        } catch (DataAccessException e) {
            res.status(500);
            return "{ \"message\": \"Error: description\" }";
        } catch (F400 f) {
            res.status(400);
            return "{ \"message\": \"Error: bad request\" }";
        } catch (F401 f) {
            res.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        }
    }

    private Object joinGame(Request req, Response res) {
        JoinGameService joinGameService = new JoinGameService();
        Gson gson = new Gson();
        JoinGameBody joinGameBody = gson.fromJson(req.body(), JoinGameBody.class);
        String authToken = req.headers("authorization");
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, joinGameBody.playerColor(), joinGameBody.gameID());
        try {
            res.status(200);
            joinGameService.joinGame(joinGameRequest);
            return "{}";
        } catch (DataAccessException e) {
            res.status(500);
            return "{ \"message\": \"Error: description\" }";
        } catch (F400 f) {
            res.status(400);
            return "{ \"message\": \"Error: bad request\" }";
        } catch (F401 f) {
            res.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        } catch (F403 f) {
            res.status(403);
            return "{ \"message\": \"Error: already taken\" }";
        }
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

//        var webDir = Paths.get(Server.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "web");
        Spark.staticFileLocation("/web");

        Spark.get("/name", this::listNames);

        Spark.delete("/db", this::clear);

        Spark.post("/user", this::register);

        Spark.post("/session", this::login);

        Spark.delete("/session", this::logout);

        Spark.get("/game", this::listGames);

        Spark.post("/game", this::createGame);

        Spark.put("/game", this::joinGame);


        // Register your endpoints and handle exceptions here.

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
    }
}
