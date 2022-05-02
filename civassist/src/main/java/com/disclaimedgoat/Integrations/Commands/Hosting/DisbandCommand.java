package com.disclaimedgoat.Integrations.Commands.Hosting;

import com.disclaimedgoat.Integrations.Data.SessionData;
import com.disclaimedgoat.Utilities.DataManagement.Logger;
import com.disclaimedgoat.Utilities.Discord.ChannelUtils;
import com.disclaimedgoat.Utilities.Discord.EventUtils;
import com.disclaimedgoat.Utilities.Discord.PermissionUtil;
import net.azzerial.slash.annotations.Option;
import net.azzerial.slash.annotations.OptionType;
import net.azzerial.slash.annotations.Slash;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

@Slash.Tag("civdisband")
@Slash.Command(
        name = "civdisband", description = "PERMANENTLY deletes session hook from Discord as well as the text channel.",
        options = {
                @Option(
                        name = "session_name",
                        description = "The name of a currently running session. Must perform this command in the session's text channel.",
                        type = OptionType.STRING,
                        required = true
                ),
        }
)
public final class DisbandCommand extends HostBaseCommand{
    @Override
    public String getCommand() {
        return "civdisband";
    }

    @Override @Slash.Handler()
    public void execute(SlashCommandEvent event) {
        //Checks if can use the host command
        if(isIllegalHosting(event)) return;

        //Get the guild and member from the command issuer
        Guild guild = event.getGuild();
        Member member = event.getMember();

        //Get the session name option mapping and use it if it exists
        OptionMapping sessionNameMapping = event.getOption("session_name");
        if(EventUtils.isNull(event, sessionNameMapping)) return;

        String sessionName = sessionNameMapping.getAsString();
        //Get the session data from session name.
        //If data is null, then there exists no session by that name. Let the user know
        // and return
        SessionData data = SessionData.getBySessionName(guild, sessionName);
        if(data == null) {
            EventUtils.sendSilentReply(event, "🛑 Cannot find session in this server with session name as `" + sessionName + "`.");
            return;
        }

        //If the user can't modify this channel, say so and return
        if(!PermissionUtil.canModifyHost(guild, member, data))  {
            EventUtils.sendSilentReply(event, "🛑 You do not have permission to modify this session!");
            return;
        }

        //Get the message channel that the event took place in
        MessageChannel channel = event.getChannel();
        //Get the channel that the session is associated with
        GuildChannel guildChannel = guild.getGuildChannelById(data.channelId);
        if(EventUtils.isNull(event, guildChannel)) return;

        //If these two channels don't equal each other,
        // let the user know and return.
        // REQUIREMENT: COMMAND CHANNEL AND SESSION NAME MUST CORRESPOND TO SAME SESSION
        if(!data.channelId.equals(channel.getId())) {
            EventUtils.sendSilentReply(event, "" +
                    "🛑 To prevent any unintentional deletions, " +
                    "you must disband this session in " + ChannelUtils.getAsMentionableChannel(guildChannel) +
                    ".");
            return;
        }

        //Otherwise, success. Delete the channel and the session from the database.
        EventUtils.sendSilentReply(event, "✅ Successfully deleted session!");

        guildChannel.delete().complete();
        SessionData.deleteSession(data);

        Logger.guildLogF(guild, "%s called to disband session '%s'", member.getEffectiveName(), sessionName);
    }

    @Override
    public String getUsage() {
        return "/civdisband <session_name>";
    }
}
