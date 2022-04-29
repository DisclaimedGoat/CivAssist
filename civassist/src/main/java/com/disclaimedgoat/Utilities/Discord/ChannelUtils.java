package com.disclaimedgoat.Utilities.Discord;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.util.EnumSet;

public class ChannelUtils {

    //Add member to be able to access the channel
    public static void addUserToChannel(GuildChannel channel, Member member) {
        channel.putPermissionOverride(member)
                .setAllow(
                        Permission.VIEW_CHANNEL,
                        Permission.MESSAGE_WRITE,
                        Permission.MESSAGE_ADD_REACTION)
                .complete();
    }

    //Remove member from accessing the channel
    public static void removeUserFromChannel(GuildChannel channel, Member member) {
        channel.putPermissionOverride(member)
                .setAllow()
                .setDeny(
                        Permission.VIEW_CHANNEL,
                        Permission.MESSAGE_WRITE,
                        Permission.MESSAGE_ADD_REACTION)
                .queue();
    }

    //Make @everyone unable to see the channel
    public static void makeChannelPrivate(GuildChannel channel) {
        channel.putPermissionOverride(channel.getGuild().getPublicRole())
                .setDeny(Permission.VIEW_CHANNEL).queue();
    }

    //Create a private channel with a default topic
    public static TextChannel createPrivateChannel(Guild guild, String name, Member startingMember, Member bot) {
        return createPrivateChannel(guild, name, "A brand new text channel", startingMember, bot);
    }

    //Create a new text channel that is only accessible (R/W) for:
    //      startingMember, bot, and server owner.
    public static TextChannel createPrivateChannel(Guild guild, String name, String topic, Member startingMember, Member bot) {
        return guild.createTextChannel(name)
                .setTopic(topic)
                .addPermissionOverride(startingMember, EnumSet.of(
                        Permission.VIEW_CHANNEL,
                        Permission.MESSAGE_WRITE,
                        Permission.MESSAGE_ADD_REACTION),
                        null)
                .addPermissionOverride(bot, EnumSet.of(
                        Permission.VIEW_CHANNEL,
                        Permission.MESSAGE_WRITE,
                        Permission.MESSAGE_ADD_REACTION),
                        null)
                .addPermissionOverride(guild.getPublicRole(),
                        null, EnumSet.of(
                                Permission.VIEW_CHANNEL))
                .complete();
    }

    //Convert channel to string that can be used as a reference in Discord's channels
    public static String getAsMentionableChannel(GuildChannel channel) {
        return String.format("<#%s>", channel.getId());
    }

    public static void archiveChannel(TextChannel channel) {
        Role publicRole = channel.getGuild().getPublicRole();
        channel.upsertPermissionOverride(publicRole)
                .setAllow(Permission.VIEW_CHANNEL)
                .setDeny(Permission.MESSAGE_WRITE)
                .complete();
    }

}
