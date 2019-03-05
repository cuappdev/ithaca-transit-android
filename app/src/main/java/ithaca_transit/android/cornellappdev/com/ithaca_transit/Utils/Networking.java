package ithaca_transit.android.cornellappdev.com.ithaca_transit.Utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public final class Networking {
    private static final String BASE_URL = "http://transit-backend.cornellappdev.com/api/v1/";

    // Returns JSON from GET request
    public static JSONObject getJSON(String append) {
        try {
            URL url = new URL(BASE_URL + append);
            BufferedReader reader = null;
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.connect();

            InputStream stream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(stream);
            reader = new BufferedReader(inputStreamReader);

            StringBuffer buffer = new StringBuffer();

            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            String json = buffer.toString();
            return new JSONObject(json);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static JSONObject postJSON(String append, HashMap<String, String> params) {
        try {

            // Preparing post data
            boolean firstItem = true;
            String args = "";
            for (HashMap.Entry<String, String> entry : params.entrySet()) {
                if (!firstItem) {
                    args = args + "&";
                } else {
                    firstItem = false;
                }
                args = args + URLEncoder.encode(entry.getKey(), "UTF-8") +
                        "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
            }
            byte[] paramBytes = args.getBytes(StandardCharsets.UTF_8);
            int length = paramBytes.length;

            // Establishing connection
            URL url = new URL(BASE_URL + append);
            BufferedReader reader = null;
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setFixedLengthStreamingMode(length);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            connection.connect();

            // Sending post data
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(paramBytes);
            }

            // Getting data returned from request
            InputStream stream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(stream);
            reader = new BufferedReader(inputStreamReader);

            StringBuffer buffer = new StringBuffer();

            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            String json = buffer.toString();
            return new JSONObject(json);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
