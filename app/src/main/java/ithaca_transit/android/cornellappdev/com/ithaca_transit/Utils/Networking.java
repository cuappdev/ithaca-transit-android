package ithaca_transit.android.cornellappdev.com.ithaca_transit.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class Networking {
    private static final String BASE_URL = "http://transit-backend.cornellappdev.com/api/v1/";

    public static JSONObject getJSON(String append){
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

        }catch (IOException e){
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }


}
