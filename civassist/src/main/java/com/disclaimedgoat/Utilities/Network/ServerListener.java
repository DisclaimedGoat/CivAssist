package com.disclaimedgoat.Utilities.Network;

import com.disclaimedgoat.Utilities.DataManagement.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

//Disclaimer! Most of this code was inspired by:
//https://www.baeldung.com/a-guide-to-java-sockets
// Check out the website and see some great guides they offer

public class ServerListener {

    private final int port;
    private final Thread thread;
    private ServerSocket serverSocket;

    public ServerListener(int port, ServerAction action) {
        this.port = port;
        this.thread = new Thread(() -> perform(action));
    }

    private void perform(ServerAction action) {
        try { serverSocket = new ServerSocket(port); }
        catch (IOException e) {
            Logger.globalErr("socket", "Encountered exception in server listener when trying to build socket!", e.getLocalizedMessage());
        }

        while(true) {
            try {
                System.out.println("Starting to listen");
                Socket socket = serverSocket.accept();

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                action.perform(out, in);
                Logger.globalLogF("socket", "Received socket data from %s.", socket.getRemoteSocketAddress().toString());
            } catch (IOException e) {
                Logger.globalErr("socket", "Encountered exception in server listener!", e.getLocalizedMessage());
            }
        }
    }

    public void start() { thread.start(); }
    public void stop() { thread.interrupt(); }
    public boolean isListening() { return thread.isAlive(); }

    public interface ServerAction {
        void perform(PrintWriter out, BufferedReader in) throws IOException;
    }
}
