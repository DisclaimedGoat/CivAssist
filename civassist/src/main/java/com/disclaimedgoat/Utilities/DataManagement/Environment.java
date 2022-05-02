package com.disclaimedgoat.Utilities.DataManagement;

import io.github.cdimascio.dotenv.Dotenv;

public final class Environment {
    private static final Dotenv env = Dotenv.load();

    private static String discordToken = "";

    private static String mongoDBUsername= "";
    private static String mongoDBPassword = "";

    private static String logRootPath = "";

    private static int serverPort = 0;


    public static String getDiscordToken() {
        return discordToken;
    }

    public static String getMongoDBUsername() {
        return mongoDBUsername;
    }

    public static String getMongoDBPassword() {
        return mongoDBPassword;
    }

    public static String getLogRootPath() {
        return logRootPath;
    }

    public static int getServerPort() { return serverPort; }

    public static void init() {
        discordToken = env.get("DISCORD_TOKEN");

        mongoDBUsername = env.get("MONGODB_USERNAME");
        mongoDBPassword = env.get("MONGODB_PASSWORD");

        logRootPath = env.get("LOG_ROOT_PATH");

        serverPort = Integer.parseInt(env.get("PORT"));
    }
}
