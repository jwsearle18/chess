package server;

import Failures.F400;
import Failures.F401;
import Failures.F403;
import com.google.gson.Gson;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import requests.LoginRequest;
import requests.LogoutRequest;
import requests.RegisterRequest;
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
            return "";
        }

        catch(DataAccessException E) {
            res.status(500);
            return "{ \"message\": \"Error: description\" }";
        }
    }
//    private Object logout(Request req, Response res) {
//        String authToken = req.headers("Authorization");
//    }
    private Object register(Request req, Response res) {
//        System.out.println(req.body());
        RegisterService register = new RegisterService();
        Gson gson = new Gson();
        UserData userData = gson.fromJson(req.body(), UserData.class);
        try {
            res.status(200);
            AuthData result = register.register(userData);
            return gson.toJson(result);
        } catch (DataAccessException e){
            res.status(500);
            return "{ \"message\": \"Error: description\" }";
        } catch (F400 f){
            res.status(400);
            return "{ \"message\": \"Error: bad request\" }";
        } catch (F403 e) {
            res.status(403);
            return "{ \"message\": \"Error: already taken\" }";
        }
    }

    private Object login(Request req, Response res) {
        LoginService login = new LoginService();
        Gson gson = new Gson();
        LoginRequest loginRequest = gson.fromJson(req.body(), LoginRequest.class);
        try {
            res.status(200);
            AuthData result = login.login(loginRequest);
            return gson.toJson(result);
        } catch (DataAccessException e) {
            res.status(500);
            return "{ \"message\": \"Error: description\" }";
        } catch (F401 f) {
            res.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        }
    }

    private Object logout(Request req, Response res) {
        LogoutService logout = new LogoutService();
        Gson gson = new Gson();
        LogoutRequest logoutRequest = gson.fromJson(req.headers("Authorization"), LogoutRequest.class);
        try {
            res.status(200);
            logout.logout(logoutRequest);
            return "";
        } catch (DataAccessException e) {
            res.status(500);
            return "{ \"message\": \"Error: description\" }";
        } catch (F401 f) {
            res.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
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


        // Register your endpoints and handle exceptions here.

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
    }
}
