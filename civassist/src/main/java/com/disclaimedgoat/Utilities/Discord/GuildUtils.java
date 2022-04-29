package com.disclaimedgoat.Utilities.Discord;

import com.disclaimedgoat.Main;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public class GuildUtils {

    public static Member getMemberFromUser(Guild guild, User user) {
        return guild.getMember(user);
    }

    public static Member getBotAsMember(Guild guild) {
        return getMemberFromUser(guild, Main.getJda().getSelfUser());
    }

    public static void iterateGuilds(GuildIterator iterator) {
        for(Guild guild : Main.getJda().getGuilds())
            iterator.iterate(guild);
    }

    public interface GuildIterator {
        void iterate(Guild guild);
    }

}
