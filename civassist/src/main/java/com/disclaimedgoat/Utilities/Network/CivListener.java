package com.disclaimedgoat.Utilities.Network;

import com.disclaimedgoat.Integrations.Data.SessionData;
import com.disclaimedgoat.Utilities.DataManagement.Logger;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.azzerial.slash.util.Session;
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

            StringBuilder stringBuilder = new StringBuilder();
            out.lines().forEach(stringBuilder::append);
            String content = stringBuilder.toString();
            System.out.println(content);

//            JsonElement jsonElement = JsonParser.parseString(content);
//            if(!jsonElement.isJsonObject()) {
//                System.out.println("Not a valid json object!");
//                return;
//            }
//            JsonObject jsonObject = jsonElement.getAsJsonObject();
//
//            String gameName = jsonObject.get("value1").getAsString();
//            String playerName = jsonObject.get("value2").getAsString();
//            String turnNumber = jsonObject.get("value3").getAsString();
//
//            SessionData sessionData = SessionData.findFirst(gameName);
//            if(gameName == null) {
//                System.out.println("Unable to find session name in database. Skipping");
//                return;
//            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
