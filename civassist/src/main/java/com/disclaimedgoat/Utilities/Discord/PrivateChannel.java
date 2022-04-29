package com.disclaimedgoat.Utilities.Discord;

import net.dv8tion.jda.api.entities.User;
// import usu.crypto.Data.Frameworks.Logger;

public final class PrivateChannel {
    public static void sendPrivateMessage(User user, String content) {
        user.openPrivateChannel().queue((channel) ->
        {
            // Logger.log("Private channel connection to user " + user.getName() + " has been made. Sending message: " + content.substring(0, 24) + "...");
            channel.sendMessage(content).queue();
        });
    }
}
