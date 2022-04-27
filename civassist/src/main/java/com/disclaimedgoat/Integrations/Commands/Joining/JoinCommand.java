package com.disclaimedgoat.Integrations.Commands.Joining;

import com.disclaimedgoat.Integrations.Commands.BaseCommand;
import com.disclaimedgoat.Integrations.Data.SessionData;
import com.disclaimedgoat.Integrations.Data.UserData;
import com.disclaimedgoat.Utilities.ChannelUtils;
import com.disclaimedgoat.Utilities.EventUtils;
import com.disclaimedgoat.Utilities.MemberUtils;
import net.azzerial.slash.annotations.Option;
import net.azzerial.slash.annotations.Slash;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import static net.azzerial.slash.annotations.OptionType.STRING;

@Slash.Tag("civjoin")
@Slash.Command(
        name = "civjoin", description = "Join an active session that is not full.",
        options = {
                @Option(
                        name = "session_name",
                        description = "The name of the session to join. Must be spelled exactly, including symbols and capitalization.",
                        type = STRING,
                        required = true
                ),
        }
)
public final class JoinCommand extends BaseCommand {
    @Override
    public String getCommand() {
        return "civjoin";
    }

    @Override @Slash.Handler()
    public void execute(SlashCommandEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        if(EventUtils.isNull(event, guild, member)) return;

        if(!UserData.isUserRegistered(member.getId())) {
            EventUtils.sendSilentReply(event, "🛑 You must register your Civ6 game name before you can join a session! Do so with `/civreg`");
            return;
        }
        UserData data = UserData.getUser(member.getId());

        OptionMapping sessionNameOption = event.getOption("session_name");
        if(EventUtils.isNull(event, sessionNameOption)) return;
        String sessionName = sessionNameOption.getAsString();

        SessionData sessionData = SessionData.getBySessionName(guild, sessionName);
        if(sessionData == null) {
            EventUtils.sendSilentReply(event, "🛑 Cannot find session in this server with session name as `" + sessionName + "`.");
            return;
        }

        if(sessionData.hostId.equals(member.getId())) {
            EventUtils.sendSilentReply(event, "🛑 You have already joined this session since you are the host silly!");
            return;
        }

        TextChannel channel = guild.getTextChannelById(sessionData.channelId);
        if(EventUtils.isNull(event, channel)) return;

        if(!sessionData.addJoinedUser(member.getId())) {
            EventUtils.sendSilentReply(event, "🛑 You have already joined this session! If you'd like to leave, do so with `/civjoin leave`.");
            return;
        }

        ChannelUtils.addUserToChannel(channel, member);
        channel.sendMessage(String.format("%s has just joined this session. Playing as player: `%s`.",
                MemberUtils.memberToMentionable(member),
                data.playerName)).queue();

        EventUtils.sendSilentReply(event, "✅ Successfully joined session " + sessionName + ". You are playing as " + data.playerName);
    }

    @Override
    public String getUsage() {
        return "/civjoin [session_name]";
    }
}
