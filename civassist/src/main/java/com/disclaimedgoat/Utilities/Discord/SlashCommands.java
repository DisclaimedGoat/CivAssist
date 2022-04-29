package com.disclaimedgoat.Utilities.Discord;

import com.disclaimedgoat.Integrations.Commands.BaseCommand;
import com.disclaimedgoat.Main;
import com.disclaimedgoat.Utilities.DataManagement.Logger;
import com.disclaimedgoat.Utilities.Discord.PermissionUtil;
import net.azzerial.slash.SlashClient;
import net.azzerial.slash.SlashClientBuilder;
import net.azzerial.slash.SlashCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.List;

public class SlashCommands {

    private static SlashClientBuilder builder;
    private static SlashClient client;


    public static void init(JDA jda) {

        builder = SlashClientBuilder.create(jda);
    }

    public static void addCommand(BaseCommand command) {
        builder.addCommand(command);
    }

    public static void buildCommands() {
        client = builder.build();
    }

    public static void addCommandToGuilds(String commandName) {
        for(Guild guild : Main.getJda().getGuilds()) {
            addGuildCommand(guild, commandName);
        }
    }

    public static void addGuild(Guild guild) {
        for(String commandName : BaseCommand.getCommands()) {
            addGuildCommand(guild, commandName);
        }
        updatePrivileges();
    }

    private static void addGuildCommand(Guild guild, String commandName) {
        SlashCommand command = client.getCommand(commandName);
        command.upsertGuild(guild);
    }

    public static void updatePrivileges() {
        List<Guild> guilds = Main.getJda().getGuilds();

        for(Guild guild : guilds) {
            updatePrivileges(guild);
        }
    }

    public static void updatePrivileges(Guild guild) {
        String[] commands = BaseCommand.getCommands();

        Logger.guildLog(guild, "Updating slash command privileges");

        for(String commandName : commands) {
            CommandPrivilege[] privileges = PermissionUtil.getCommandPrivileges(guild, commandName);

            SlashCommand command = client.getCommand(commandName);

            RestAction<?> restAction = command.updateGuildPrivileges(guild, privileges);
            if(restAction == null) continue;

            restAction.queue(success -> {}, failure -> {});
        }
    }

}
