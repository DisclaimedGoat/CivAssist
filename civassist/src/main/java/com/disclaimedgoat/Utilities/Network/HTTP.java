package com.disclaimedgoat.Utilities.Network;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public final class HTTP {

    public static String makeRequest(String endpoint) throws IOException {

        URL url = new URL(endpoint);

        HttpURLConnection http = (HttpURLConnection) url.openConnection();

        BufferedReader in = new BufferedReader(
            new InputStreamReader(http.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        http.disconnect();
        return content.toString();
    }

    public static boolean checkURL(String url) {
        try{
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
