package com.disclaimedgoat.Utilities.Discord;

import com.disclaimedgoat.Utilities.DataManagement.Logger;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class EventUtils {

    public static void sendSilentReply(SlashCommandEvent event, String message) {
        event.reply(message).setEphemeral(true).queue();
    }

    public static void sendError(SlashCommandEvent event) {
        event.reply("Something went horribly wrong. Please alert server staff").setEphemeral(true).queue();

        Logger.guildWarn(event.getGuild(), "An error occurred!");
    }

    public static boolean isNull(SlashCommandEvent event, Object...objs) {

        for(Object obj : objs) {
            if(obj == null) {
                sendError(event);
                Logger.guildErr(event.getGuild(), "Object was null!");

                return true;
            }
        }

        return false;
    }

}
