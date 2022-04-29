package com.disclaimedgoat.Utilities.DataManagement;

import net.dv8tion.jda.api.entities.Guild;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Logger {

    private static String logRoot = "";
    private static FileManager fileManager = null;

    private static final String dateFormat = "mm-dd-yyyy @ HH:mm:ss";
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

    private static final String logExtension = "kiko";

    private static final String globalLogsPath = "Global Logs";
    private static final String guildsLogsPath = "Guild Logs";

    public static void init() {
        logRoot = Environment.getLogRootPath();
        fileManager = new FileManager(logRoot);

        fileManager.createDirectory(globalLogsPath);
        fileManager.createDirectory(guildsLogsPath);
    }

    private static String buildLogHeader(LogLevel level) {
        return level.display + " " + buildTimeStamp() + " - ";
    }

    private static String buildTimeStamp() {
        return simpleDateFormat.format(new Date());
    }

    private static File getGlobalFile(String filename) {
        return fileManager.getFile(globalLogsPath, ext(filename));
    }
    private static File getGuildFile(Guild guild) {
        return fileManager.getFile(guildsLogsPath, ext(guild.getId()));
    }

    private static void guildWrite(Guild guild, LogLevel level, String... messages) {
        File file = getGuildFile(guild);
        for(String s : messages) {
            FileManager.printToFile(file, buildLogHeader(level) + s);
        }
    }

    private static void globalWrite(String filename, LogLevel level, String... messages) {
        File file = getGlobalFile(filename);
        for(String s : messages) {
            FileManager.printToFile(file, buildLogHeader(level) + s);
        }
    }

    public static void guildLog(Guild guild, String... messages) { guildWrite(guild, LogLevel.REGULAR, messages); }
    public static void guildErr(Guild guild, String... messages) { guildWrite(guild, LogLevel.CRITICAL, messages); }
    public static void guildTest(Guild guild, String... messages) { guildWrite(guild, LogLevel.DEBUG, messages); }
    public static void guildWarn(Guild guild, String... messages) { guildWrite(guild, LogLevel.WARNING, messages); }

    public static void globalLog(String filename, String... messages) { globalWrite(filename, LogLevel.REGULAR, messages); }
    public static void globalErr(String filename, String... messages) { globalWrite(filename, LogLevel.CRITICAL, messages); }
    public static void globalTest(String filename, String... messages) { globalWrite(filename, LogLevel.DEBUG, messages); }
    public static void globalWarn(String filename, String... messages) { globalWrite(filename, LogLevel.WARNING, messages); }

    private static String ext(String filename) { return filename + "." + logExtension; }

    private enum LogLevel {
        REGULAR("[LOG]"),
        CRITICAL("[CRITICAL]"),
        DEBUG("[TEST]"),
        WARNING("[WARNING]");

        public final String display;

        LogLevel(String display) {
            this.display = display;
        }
    }
}
