package com.disclaimedgoat.Integrations.Commands.Hosting;

import com.disclaimedgoat.Integrations.Data.SessionData;
import com.disclaimedgoat.Utilities.ChannelUtils;
import com.disclaimedgoat.Utilities.EventUtils;
import com.disclaimedgoat.Utilities.PermissionUtil;
import net.azzerial.slash.annotations.Option;
import net.azzerial.slash.annotations.OptionType;
import net.azzerial.slash.annotations.Slash;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

@Slash.Tag("civarchive")
@Slash.Command(
        name = "civarchive", description = "PERMANENTLY locks a text channel and deletes the webhook. No further messages can be sent.",
        options = {
                @Option(
                        name = "session_name",
                        description = "The name of a currently running session. Must perform this command in the session's text channel.",
                        type = OptionType.STRING,
                        required = true
                ),
        }
)
public final class ArchiveCommand extends HostBaseCommand {

    @Override
    public String getCommand() {
        return "civarchive";
    }

    @Override @Slash.Handler()
    public void execute(SlashCommandEvent event) {
        //Checks if can use the host command
        if(isIllegalHosting(event)) return;

        Guild guild = event.getGuild();
        Member member = event.getMember();
        TextChannel channel = event.getTextChannel();
        if(EventUtils.isNull(event, guild, member)) return;

        OptionMapping sessionNameMapping = event.getOption("session_name");
        if(EventUtils.isNull(event, sessionNameMapping)) return;

        String sessionName = sessionNameMapping.getAsString();
        SessionData sessionData = SessionData.getBySessionName(guild, sessionName);
        if(sessionData == null) {
            EventUtils.sendSilentReply(event, "🛑 Unable to find session by the name: " + sessionName);
            return;
        }

        if(!channel.getId().equals(sessionData.channelId)) {
            EventUtils.sendSilentReply(event, "🛑 Must perform this command in the corresponding session text channel.");
            return;
        }

        if(!PermissionUtil.canModifyHost(guild, member, sessionData)) {
            EventUtils.sendSilentReply(event, "🛑 You do not have the power to control this session.");
            return;
        }

        String newChannelName = channel.getName().replace("civ6", "archived");
        String newChannelTopic = "[ARCHIVED] " + channel.getTopic();
        channel.getManager().setName(newChannelName).setTopic(newChannelTopic).complete();

        ChannelUtils.archiveChannel(channel);
        EventUtils.sendSilentReply(event, "✅ Successfully archived channel and deleted session!");

        SessionData.deleteSession(sessionData);
    }

    @Override
    public String getUsage() {
        return "/civarchive <session_name>";
    }
}
