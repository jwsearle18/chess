package ui;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerFacade {
    private final String serverURL;

    private String authToken = null;

    public ServerFacade(int port) {
        this.serverURL = "http://localhost:" + port;
    }

    public void clearAuthToken() {
        this.authToken = null;
    }

    private String sendHttpRequest(String path, String method, String content, boolean needsAuth) {
        try {
            URL url = new URL(this.serverURL + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);

            if (needsAuth && this.authToken != null) {
                conn.setRequestProperty("Authorization", this.authToken);
            }

            if (method.equals("POST") || method.equals("PUT")) {
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = content.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            } else {
                return "Server responded with code: " + responseCode;
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String joinGame(int gameID, String color) {
        if (this.authToken == null) {
            return "Unable to join game. No active session.";
        }

        String playerColorValue = color != null ? "\"" + color.toUpperCase() + "\"" : null;
        String jsonInputString = String.format(
                "{\"gameID\": %d, \"playerColor\": %s}", gameID, playerColorValue
        );

        String response = sendHttpRequest("/game", "PUT", jsonInputString, true);

        if (response.startsWith("Server responded with code:")) {
            return "Failed to join game. " + response;
        } else {
            return "Successfully joined game!";
        }
    }

    public String listGames() {
        if (this.authToken == null) {
            return "Unable to list games. No active session.";
        }
        String response = sendHttpRequest("/game", "GET", null, true);
        if (response.startsWith("Server responded with code:")) {
            return "Failed to list games. " + response;
        } else {
            return response;
        }
    }

    public String postCreateGame(String gameName) {
        if (this.authToken == null) {
            return "Unable to Create Game. No active session.";
        }
        String jsonInputString = String.format("{\"gameName\": \"%s\"}", gameName);
        String response = sendHttpRequest("/game", "POST", jsonInputString, true);

        if (response.startsWith("Server responded with code:")) {
            return "Game creation failed. " + response;
        } else {
            return "Game created successfully!";
        }
    }
    public String postLogout() {
        if (this.authToken == null) {
            return "Logout failed. No active session.";
        }
        String response = sendHttpRequest("/session", "DELETE", null, true);
        if (response.startsWith("Server responded with code:")) {
            return "Logout failed. " + response;
        } else {
            this.authToken = null;
            return "Logout successful!";
        }
    }


    public String postLogin(String username, String password) {
        String jsonInputString = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
        String response = sendHttpRequest("/session", "POST", jsonInputString, false);

        if (response.startsWith("Server responded with code:")) {
            return "Login failed. " + response;
        } else {
            JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
            this.authToken = jsonResponse.get("authToken").getAsString();
            return "Login successful!";
        }
    }


    public String postRegister(String username, String password, String email) {
        String jsonInputString = String.format(
                "{\"username\": \"%s\", \"password\": \"%s\", \"email\": \"%s\"}",
                username, password, email
        );

        String response = sendHttpRequest("/user", "POST", jsonInputString, false);

        if (response.startsWith("Server responded with code:")) {
            return "Registration failed. " + response;
        } else {
            JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
            if (jsonResponse.has("authToken")) {
                this.authToken = jsonResponse.get("authToken").getAsString();
                return "Registration successful!";
            } else {
                return "Registration successful, but no authToken received.";
            }
        }
    }
}
