package com.isamrs.onlinehealth.utils;

import com.isamrs.onlinehealth.model.Location;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LocationFinder {
    public static Location locate(String address){
        Location location = null;

        try {
            URL url = new URL(
                    "https://nominatim.openstreetmap.org/search?format=json&q="
                            + java.net.URLEncoder.encode(address, StandardCharsets.UTF_8));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                return null;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output = "";
            StringBuilder full = new StringBuilder();
            while ((output = br.readLine()) != null) {
                full.append(output);
            }
            String toParse = full.substring(0, full.length()-1);
            JSONObject json = new JSONObject(toParse.substring(1));

            String lat = "", lon = "";
            lat = json.getString("lat");
            lon = json.getString("lon");

            if(!lat.equals("") && !lon.equals("")) {
                location = new Location(null, Double.parseDouble(lat), Double.parseDouble(lon), address);
                location.setDeleted(false);
            }
            conn.disconnect();
        }   catch (Exception e) {
           return null;
        }
        return location;
    }
}
