package com.disclaimedgoat.Integrations.Events;

import com.disclaimedgoat.Utilities.Database;
import com.disclaimedgoat.Main;
import com.disclaimedgoat.Utilities.SlashCommands;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildEvents extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        Guild guild = event.getGuild();

        Database.addServer(guild);
        SlashCommands.addGuild(guild);
    }
}
