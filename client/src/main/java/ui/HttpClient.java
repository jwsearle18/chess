package ui;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
    private static final String SERVER_URL = "http://localhost:8080";
    private String authToken = null;

    public  String postLogout() {
        try {
            URL url = new URL(SERVER_URL + "/session");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", this.authToken);

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                this.authToken = null;
                return "Logout successful!";
            } else {
                return "Logout failed. Server responded with code: " + responseCode;
            }
        } catch (Exception e) {
            return "Logout failed. Error: " + e.getMessage();
        }
    }

    public String postLogin(String username, String password) {
        try {
            URL url = new URL(SERVER_URL + "/session");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                this.authToken = jsonResponse.get("authToken").getAsString();
                return "Login successful!";
            } else {
                return "Login failed. Server responded with code: " + responseCode;
            }
        } catch (Exception e) {
            return "Login failed. Error: " + e.getMessage();
        }
    }

    public String postRegister(String username, String password, String email) {
        try {
            URL url = new URL(SERVER_URL + "/user");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = String.format("{\"username\": \"%s\", \"password\": \"%s\", \"email\": \"%s\"}",
                    username, password, email);

            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return "Registration successful!";
            } else {
                return "Registration failed. Server responded with code: " + responseCode;
            }
        } catch (Exception e) {
            return "Registration failed. Error: " + e.getMessage();
        }
    }
}
