package com.disclaimedgoat.Integrations.Commands.Hosting;

import com.disclaimedgoat.Integrations.Data.SessionData;
import com.disclaimedgoat.Integrations.Data.UserData;
import com.disclaimedgoat.Utilities.Discord.ChannelUtils;
import com.disclaimedgoat.Utilities.Discord.EventUtils;
import com.disclaimedgoat.Utilities.Discord.GuildUtils;
import net.azzerial.slash.annotations.Option;
import net.azzerial.slash.annotations.OptionType;
import net.azzerial.slash.annotations.Slash;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

@Slash.Tag("civcreate")
@Slash.Command(
        name = "civcreate", description = "Creates a new play-by-cloud session hook. Must have hosting permissions",
        options = {
            @Option(
                    name = "session_name",
                    description = "The name of the session. Must match EXACTLY with the session name in your Civilization game.",
                    type = OptionType.STRING,
                    required = true
            ),
            @Option(
                    name = "max_players",
                    description = "The max number of players. Default set to 6",
                    type = OptionType.INTEGER
            )
        }
)
public final class CreateCommand extends HostBaseCommand{

    @Override @Slash.Handler()
    public void execute(SlashCommandEvent event) {
        //Checks if can use the host command
        if(isIllegalHosting(event)) return;

        //Get the guild and member from the event
        Guild guild = event.getGuild();
        Member member = event.getMember();

        if(!UserData.isUserRegistered(member.getId())) {
            EventUtils.sendSilentReply(event, "🛑 You must register your Civ6 game name before you can join a session! Do so with `/civreg`");
            return;
        }

        //Extract the session's name. Older iteration of check
        String sessionName = event.getOption("session_name").getAsString();

        //If the player provided a max number of players, set it. Otherwise set the default to 6
        OptionMapping maxPlayersOption = event.getOption("max_players");
        int maxPlayers = 6;
        if(maxPlayersOption != null) maxPlayers = (int) maxPlayersOption.getAsLong();

        //If the session name already exists in the guild, tell the user and return.
        if(!SessionData.isValidSessionName(guild, sessionName)) {
            EventUtils.sendSilentReply(event, "🛑 A session with that name already exists. Please use a unique name. \n" +
                    "Hint: do `/list` in a general channel to see a list of active sessions.");
            return;
        }
        //Build the channel name and topic from sender data and arguments
        String channelName = "civ6-" + sessionName.toLowerCase().replace(" ", "-");
        String channelTopic = String.format(
                "A Civ6 Play-by-Cloud session hosted by %s. " +
                        "Session name is %s. " +
                        "This session can hold %d players. " +
                        "To leave, type /civjoin leave ",
                member.getEffectiveName(),
                sessionName,
                maxPlayers);

        //If the there exists a text channel with the session name already, say so and return
        if(guild.getTextChannelsByName(channelName, true).size() != 0) {
            EventUtils.sendSilentReply(event, "🛑 A channel is trying to be created yet it's name already exists. " +
                    "Please delete the channel associated with `" + sessionName + "`.");
            return;
        }

        //Create a new private text channel.
        // Starting members to access channel: server owner, the command issuer, and this bot
        TextChannel channel = ChannelUtils.createPrivateChannel(guild, channelName, channelTopic, event.getMember(), GuildUtils.getBotAsMember(guild));

        //Create new session data
        SessionData data = new SessionData(guild, sessionName, maxPlayers, member.getId(), channel.getName(), channel.getId());
        //Push the new session to the database
        if(!SessionData.pushNew(data)) {
            EventUtils.sendSilentReply(event, "🛑 Unable to push new session data to database. Please let server staff know!");
            return;
        }

        //Send a success message in the new channel and a silent reply to the command issuer.
        channel.sendMessage("A new Civilization 6 Play by Cloud session has been created! Tell your friends to join with `/civjoin join`!").queue();
        EventUtils.sendSilentReply(event, "✅ Successfully created new session!");
    }

    @Override
    public String getCommand() {
        return "civcreate";
    }

    @Override
    public String getUsage() {
        return "/civcreate [session_name] (max_players)";
    }
}
