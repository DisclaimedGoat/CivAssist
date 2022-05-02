package com.disclaimedgoat.Utilities.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static com.disclaimedgoat.Utilities.Network.ServerListener.ServerAction;

public class CivListener implements ServerAction {
    @Override
    public void perform(PrintWriter out, BufferedReader in) throws IOException {
        System.out.println("Getting a call");

        String line = in.readLine();
        while(line != null && !line.equals("\r")) {
            System.out.println(line);
            line = in.readLine();
        }
    }
}
