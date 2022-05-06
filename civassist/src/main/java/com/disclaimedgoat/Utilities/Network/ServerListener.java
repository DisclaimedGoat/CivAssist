package com.disclaimedgoat.Utilities.Network;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.thread.QueuedThreadPool;


public class ServerListener {

    private final static QueuedThreadPool threadPool = new QueuedThreadPool();
    private final Server server = new Server(threadPool);
    private final ContextHandlerCollection contextCollection = new ContextHandlerCollection();

    public ServerListener(int port) {
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.addConnector(connector);

        server.setHandler(contextCollection);
    }

    public ServerListener pushHandler(String target, AbstractHandler handler) {
        ContextHandler contextHandler = new ContextHandler(target);
        contextHandler.setHandler(handler);

        contextCollection.addHandler(contextHandler);

        return this;
    }

    public void start() throws Exception { server.start(); }
    public void stop() throws Exception { server.stop(); }
}
