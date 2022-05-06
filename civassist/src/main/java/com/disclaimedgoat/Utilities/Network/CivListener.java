package com.disclaimedgoat.Utilities.Network;

import com.disclaimedgoat.Utilities.DataManagement.Logger;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.BufferedReader;
import java.io.IOException;

public class CivListener extends AbstractHandler {

    @Override
    public void handle(String target, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        request.setHandled(true);
        httpServletResponse.setStatus(200);
        Logger.globalLogF("server", "Receiving request from '%s'", request.getRemoteHost());

        try {
            BufferedReader out = httpServletRequest.getReader();
            JsonObject jsonObject = JsonParser.parseReader(out).getAsJsonObject();
            System.out.println(jsonObject.getAsString());

            String gameName = jsonObject.get("value1").getAsString();
            String playerName = jsonObject.get("value2").getAsString();
            String turnNumber = jsonObject.get("value3").getAsString();

            System.out.printf("It's %s's turn for game %s. Turn number: %s%n", playerName, gameName, turnNumber);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
