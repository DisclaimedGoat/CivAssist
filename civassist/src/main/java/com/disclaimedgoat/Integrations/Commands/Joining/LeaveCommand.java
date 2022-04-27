package com.disclaimedgoat.Integrations.Commands.Joining;

import com.disclaimedgoat.Integrations.Commands.BaseCommand;
import com.disclaimedgoat.Integrations.Data.SessionData;
import com.disclaimedgoat.Utilities.ChannelUtils;
import com.disclaimedgoat.Utilities.EventUtils;
import com.disclaimedgoat.Utilities.MemberUtils;
import net.azzerial.slash.annotations.Slash;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@Slash.Tag("civleave")
@Slash.Command(
        name = "civleave",
        description = "Leave a session that you are currently joined to. Must be ran in the text channel you want to leave."

)
public final class LeaveCommand extends BaseCommand {
    @Override
    public String getCommand() {
        return "civleave";
    }

    @Override @Slash.Handler()
    public void execute(SlashCommandEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        if(EventUtils.isNull(event, guild, member)) return;

        SessionData sessionData = SessionData.getByChannelId(guild, event.getChannel().getId());
        if(sessionData == null) {
            EventUtils.sendSilentReply(event, "🛑 Couldn't leave session. Ensure you are using this command in the session channel that you want to leave from!");
            return;
        }

        if(!sessionData.removeJoinedUser(member.getId())) {
            EventUtils.sendError(event);
            return;
        }

        TextChannel channel = guild.getTextChannelById(sessionData.channelId);
        ChannelUtils.removeUserFromChannel(channel, member);
        channel.sendMessage(String.format("%s has left this session.",
                MemberUtils.memberToMentionable(member))).queue();

        EventUtils.sendSilentReply(event, "✅ Successfully left session `" + sessionData.sessionName + "`");
    }

    @Override
    public String getUsage() {
        return "/civleave";
    }
}
