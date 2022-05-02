package com.disclaimedgoat;

import com.disclaimedgoat.Integrations.Commands.BaseCommand;
import com.disclaimedgoat.Integrations.Events.GuildEvents;
import com.disclaimedgoat.Utilities.DataManagement.Database;
import com.disclaimedgoat.Utilities.DataManagement.Environment;
import com.disclaimedgoat.Utilities.DataManagement.Logger;
import com.disclaimedgoat.Utilities.Network.CivListener;
import com.disclaimedgoat.Utilities.Network.HTTP;
import com.disclaimedgoat.Utilities.Network.ServerListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import java.io.IOException;

public final class Main {

    // Permissions int = 275146493008
    // invite bot:
    // https://discord.com/api/oauth2/authorize?client_id=965744215944466452&permissions=275146493008&scope=bot%20applications.commands

    //DO NOT SHARE THIS WITH ANYONE!!!
    private static String TOKEN = "";

    private static JDA jda = null;
    public static JDA getJda() { return jda; }
    private static ServerListener serverListener;

    public static void main(String[] args) {
        Environment.init();
        Logger.init();
        Database.init();

        TOKEN = Environment.getDiscordToken();
        setupJDA();

        BaseCommand.init();
        setupListeners();

        //Setup the listening part of the server
        serverListener = new ServerListener(8080, new CivListener());
        serverListener.start();

//        try { System.out.println(HTTP.makeRequest("http://localhost:8080")); }
//        catch(IOException ignored) { }
    }

    private static void setupJDA() {
        try {
            Logger.globalLog("jda", "Building JDA");
            jda = JDABuilder.createLight(TOKEN).build().awaitReady();
            Logger.globalLog("jda", "JDA Successfully built");

            jda.getPresence().setActivity(Activity.listening("/civhelp 💖"));

        } catch (Exception e) {
            Logger.globalErr("jda", "Error occurred when building JDA", e.getMessage());
        }
    }

    private static void setupListeners() {
        jda.addEventListener(new GuildEvents());
    }
}