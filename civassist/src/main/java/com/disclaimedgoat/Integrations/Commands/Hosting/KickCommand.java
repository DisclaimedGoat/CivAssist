package com.disclaimedgoat.Integrations.Commands.Hosting;

import com.disclaimedgoat.Integrations.Data.SessionData;
import com.disclaimedgoat.Main;
import com.disclaimedgoat.Utilities.ChannelUtils;
import com.disclaimedgoat.Utilities.EventUtils;
import com.disclaimedgoat.Utilities.PermissionUtil;
import net.azzerial.slash.annotations.Option;
import net.azzerial.slash.annotations.Slash;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.ArrayList;

import static net.azzerial.slash.annotations.OptionType.*;

@SuppressWarnings("ConstantConditions")
@Slash.Tag("civkick")
@Slash.Command(
        name = "civkick", description = "Kicks a user from a current session. Must be ran in a session's text channel.",
        options = {
                @Option( name = "user", description = "Kick this user", type = USER, required = true),
                @Option( name = "user1", description = "Kick this user", type = USER),
                @Option( name = "user2", description = "Kick this user", type = USER),
                @Option( name = "user3", description = "Kick this user", type = USER),
                @Option( name = "user4", description = "Kick this user", type = USER),
                @Option( name = "user5", description = "Kick this user", type = USER),
                @Option( name = "user6", description = "Kick this user", type = USER),
                @Option( name = "user7", description = "Kick this user", type = USER),
                @Option( name = "user8", description = "Kick this user", type = USER),
                @Option( name = "user9", description = "Kick this user", type = USER),
                @Option( name = "user10", description = "Kick this user", type = USER),
                @Option( name = "user11", description = "Kick this user", type = USER),
                @Option( name = "user12", description = "Kick this user", type = USER),
                @Option( name = "user13", description = "Kick this user", type = USER),
                @Option( name = "user14", description = "Kick this user", type = USER)

        }
)
public final class KickCommand extends HostBaseCommand{

    @Override
    public String getCommand() {
        return "civkick";
    }

    @Override @Slash.Handler()
    public void execute(SlashCommandEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        TextChannel channel = event.getTextChannel();
        if(EventUtils.isNull(event, guild, member, channel)) return;

        SessionData sessionData = SessionData.getByChannelId(guild, channel.getId());
        if(sessionData == null) {
            EventUtils.sendSilentReply(event, "🛑 This channel is not associated with a session.");
            return;
        }

        if(!PermissionUtil.canModifyHost(guild, member, sessionData)) {
            EventUtils.sendSilentReply(event, "🛑 You do not have the power to control this session.");
            return;
        }

        OptionMapping primaryUserMapping = event.getOption("user");
        if(EventUtils.isNull(event, primaryUserMapping)) return;

        User primaryUser = primaryUserMapping.getAsUser();
        if(isUserImportant(guild, primaryUser, sessionData.hostId)) {
            EventUtils.sendSilentReply(event, "🛑 Cannot remove " + primaryUser.getName() + " from session due to their permission level.");
            return;
        }

        ArrayList<User> kickedUsers = new ArrayList<>();
        kickedUsers.add(primaryUser);

        sessionData.removeJoinedUser(primaryUser.getId());
        ChannelUtils.removeUserFromChannel(channel, guild.retrieveMember(primaryUser).complete());

        String message = member.getEffectiveName() + " kicked " + primaryUser.getName() + " from session.";
        String reply = "";

        for(int i = 1; i < 15; i++) {
            OptionMapping potentialUserMapping = event.getOption("user" + i);
            if(potentialUserMapping == null) continue;

            User kickUser = potentialUserMapping.getAsUser();
            if(kickedUsers.contains(kickUser)) continue;
            if(isUserImportant(guild, kickUser, sessionData.hostId)) {
                reply += "\n🛑 Cannot remove " + kickUser.getName() + " from session due to their permission level. Skipping.";
                continue;
            }

            sessionData.joinedUsers.remove(kickUser.getId());
            ChannelUtils.removeUserFromChannel(channel, guild.retrieveMember(kickUser).complete());

            message += "\n" + member.getEffectiveName() + " kicked " + primaryUser.getName() + " from session.";
        }

        channel.sendMessage(message).queue();

        reply += "\n" + "✅ Successfully kicked user(s).";
        EventUtils.sendSilentReply(event, reply);
    }

    @Override
    public String getUsage() {
        return "/civkick <user> (user...)";
    }

    private boolean isUserImportant(Guild guild, User user, String hostId) {
        return guild.getOwnerId().equals(user.getId()) ||
                hostId.equals(user.getId()) ||
                user.getId().equals(Main.getJda().getSelfUser().getId());
    }
}
