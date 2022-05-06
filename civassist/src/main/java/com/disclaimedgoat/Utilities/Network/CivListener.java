package com.disclaimedgoat.Utilities.Network;

import com.disclaimedgoat.Utilities.DataManagement.Logger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class CivListener extends AbstractHandler {

    @Override
    public void handle(String target, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        request.setHandled(true);
        httpServletResponse.setStatus(200);
        Logger.globalLogF("server", "Receiving request from '%s'", request.getRemoteAddr());

        System.out.println(httpServletResponse.getContentType());
    }
}
