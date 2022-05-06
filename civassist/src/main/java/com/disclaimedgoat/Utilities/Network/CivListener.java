package com.disclaimedgoat.Utilities.Network;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class CivListener extends AbstractHandler {

    @Override
    public void handle(String target, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        request.setHandled(true);

        System.out.println(httpServletRequest.toString());
        httpServletResponse.setStatus(200);
    }
}
