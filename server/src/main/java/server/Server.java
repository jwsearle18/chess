package server;

import com.google.gson.Gson;
import requests.RegisterRequest;
import results.RegisterResult;
import spark.*;

import java.nio.file.Paths;

public class Server {


    private Object listNames(Request req, Response res) {
        res.type("application/json");
        return "{}";
    }

    private Object clear(Request req, Response res) {
        res.status(500);
        return "{ \"message\": \"Error: description\" }";
    }

    private Object register(Request req, Response res) {
        System.out.println(req.body());
        Gson gson = new Gson();
        RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);

        //call service to do registration
        //RegisterResult result = service.register(request);

        RegisterResult result = new RegisterResult();
        result.username = "jaden";
        result.authToken = "1234";

        res.status(200);


        return gson.toJson(result);
    }



    public int run(int desiredPort) {
        Spark.port(desiredPort);

//        var webDir = Paths.get(Server.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "web");
        Spark.staticFileLocation("/web");

        Spark.get("/name", this::listNames);

        Spark.delete("/db", this::clear);

        Spark.post("/user", this::register);


        // Register your endpoints and handle exceptions here.

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
    }
}
