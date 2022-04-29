package com.disclaimedgoat.Integrations.Events;

import com.disclaimedgoat.Utilities.DataManagement.Database;
import com.disclaimedgoat.Utilities.DataManagement.Logger;
import com.disclaimedgoat.Utilities.Discord.SlashCommands;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildEvents extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        Guild guild = event.getGuild();

        Database.addServer(guild);
        SlashCommands.addGuild(guild);

        Logger.globalLog("events", "New guild join event: " + guild.getId());
    }
}
