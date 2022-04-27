package com.disclaimedgoat.Integrations.Commands.Tools;

import com.disclaimedgoat.Integrations.Commands.BaseCommand;
import com.disclaimedgoat.Integrations.Data.SessionData;
import com.disclaimedgoat.Integrations.Data.UserData;
import com.disclaimedgoat.Utilities.*;
import net.azzerial.slash.annotations.Slash;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.ArrayList;
import java.util.List;

@Slash.Tag("civlist")
@Slash.Command(
        name = "civlist",
        description = "Get a detailed list of information depending where you execute this command."
)
public final class ListCommand extends BaseCommand {
    @Override
    public String getCommand() {
        return "civlist";
    }

    @Override @Slash.Handler()
    public void execute(SlashCommandEvent event) {
        //Get the guild and member of the command issuer, and require them to not be null
        Guild guild = event.getGuild();
        Member member = event.getMember();
        if(EventUtils.isNull(event, guild, member)) return;

        //Get the channel that the command was issued in
        MessageChannel channel = event.getChannel();

        //See if there exists a session with this channel's id
        SessionData sessionData = SessionData.getByChannelId(guild, channel.getId());

        //Build the basic embed for the bot
        EmbedBuilder embed = EmbedUtils.buildClassic(member.getUser());
        //Absolute means if the issuer has absolute power of the bot in this server
        final boolean isMemberAbsolute = PermissionUtil.hasAbsolute(guild, member);

        //Is not in a channel associated with a session,
        // Print all of the sessions that the user is not blacklisted in
        if(sessionData == null) {
            embed.setTitle("List of Sessions in Server:");
            embed.appendDescription("\nEnter `/civjoin join [SESSION NAME]` to join an open session\n\n");

            //Simple boolean represented as array for lambda function. Still acts as normal boolean variable.
            final boolean[] hasSessions = {false};

            //For each session in server collection
            Database.iterateCollection(guild, doc -> {
                hasSessions[0] = true;
                //Get all of the necessary data to add to the embed.
                List<String> blacklistedUsers = doc.getList("blacklistedUsers", String.class);
                if(!isMemberAbsolute && blacklistedUsers.contains(member.getId())) return;

                List<String> joinedUsers = doc.getList("joinedUsers", String.class);
                int numJoinedPlayers = joinedUsers.size();

                String sessionName = doc.getString("sessionName");
                int maxPlayers = doc.getInteger("maxPlayers");

                String sessionStatus = "🟢";
                if(numJoinedPlayers == maxPlayers)
                    sessionStatus = "🟡";
                else if(numJoinedPlayers >= maxPlayers)
                    sessionStatus = "🔴";

                embed.appendDescription(String.format("> %s **%s** (%d/%d)\n",
                        sessionStatus, sessionName, numJoinedPlayers, maxPlayers));
            });
            //If no sessions exist in the server, say so in the embed.
            if(!hasSessions[0]) {
                embed.appendDescription("No active sessions in server.");
            }
        } else {
            //Otherwise, print the users in the session of this channel
            String sessionName = sessionData.sessionName;
            ArrayList<String> players = sessionData.joinedUsers;
            int numPlayers = players.size();
            int maxPlayers = sessionData.maxPlayers;
            //If the command issuer is either the server owner of the host of this session
            boolean hasPowerInSession = isMemberAbsolute || member.getId().equals(sessionData.hostId);

            embed.setTitle(sessionName);
            embed.appendDescription(String.format("**Players (%d/%d): **\n",  numPlayers, maxPlayers));

            for(String userId : players) {
                boolean isImportant = PermissionUtil.hasAbsolute(guild, userId) || userId.equals(sessionData.hostId);
                String bulletCharacter = isImportant ? "+" : "-";
                UserData data = UserData.getUser(userId);
                embed.appendDescription(String.format("%s %s (%s)\n", bulletCharacter, MemberUtils.memberToMentionable(userId), data.playerName));
            }

            //If this user has power, then print the blacklisted users.
            if(hasPowerInSession) {
                embed.appendDescription("\n**Blacklisted Users: **\n");

                if(sessionData.blacklistedUsers.size() < 1)
                    embed.appendDescription("No blacklisted users");

                for(String userId : sessionData.blacklistedUsers)
                    embed.appendDescription(String.format("- %s\n", MemberUtils.memberToMentionable(userId)));
            }
        }

        //Get the build embed, depending on the criteria above, and send it back to the issuer.
        EmbedUtils.appendFooterBar(embed);
        event.replyEmbeds(embed.build()).setEphemeral(true).complete();
    }

    @Override
    public String getUsage() {
        return "civlist";
    }
}
