package com.disclaimedgoat;

import com.disclaimedgoat.Integrations.Commands.BaseCommand;
import com.disclaimedgoat.Integrations.Events.GuildEvents;
import com.disclaimedgoat.Utilities.Database;
import com.disclaimedgoat.Utilities.Environment;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import java.io.File;
import java.io.IOException;

public final class Main {

    // Permissions int = 275146493008
    // invite bot:
    // https://discord.com/api/oauth2/authorize?client_id=965744215944466452&permissions=275146493008&scope=bot%20applications.commands

    //DO NOT SHARE THIS WITH ANYONE!!!
    private static String TOKEN = "";

    private static JDA jda = null;
    public static JDA getJda() { return jda; }

    public static void main(String[] args) {
        Environment.init();
        TOKEN = Environment.getDiscordToken();

        Database.init();

        setupJDA();

        BaseCommand.init();
        setupListeners();
    }

    private static void setupJDA() {
        try {

            jda = JDABuilder.createLight(TOKEN).build().awaitReady();

            jda.getPresence().setActivity(Activity.listening("/civhelp 💖"));
        } catch (Exception e) {
            System.out.println("Oops\n" + e.getMessage());
        }
    }

    private static void setupListeners() {
        jda.addEventListener(new GuildEvents());
    }
}