package ui;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
    private static final String SERVER_URL = "http://localhost:8080";

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
                return "Login successful!";
            } else {
                // Handle failure
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
