package com.disclaimedgoat.Utilities.Discord;

import com.disclaimedgoat.Constants;
import com.disclaimedgoat.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class EmbedUtils {

    public static EmbedBuilder buildClassic(User user) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Constants.BOT_COLOR);

        String avatarUrl = user.getAvatarUrl();
        embed.setAuthor(user.getAsTag(), avatarUrl, avatarUrl);

        embed.setFooter("made and sent with love 💖", Main.getJda().getSelfUser().getAvatarUrl());

        return embed;
    }

    public static void appendFooterBar(EmbedBuilder embed) {
        embed.appendDescription("\n----------------------------------------------------");
    }

}
