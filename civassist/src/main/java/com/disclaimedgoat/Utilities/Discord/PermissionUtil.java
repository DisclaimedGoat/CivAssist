package com.disclaimedgoat.Utilities.Discord;

import com.disclaimedgoat.Integrations.Commands.BaseCommand;
import com.disclaimedgoat.Integrations.Data.SessionData;
import com.disclaimedgoat.Main;
import com.disclaimedgoat.Utilities.DataManagement.Logger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;

import java.util.List;

import static net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege.Type.ROLE;
import static net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege.Type.USER;

public class PermissionUtil {

    //If the bot cannot interact with this person's roles, then they can host
    public static boolean canHost(Guild guild, Member member) {
        Member bot = guild.getMember(Main.getJda().getSelfUser());
        boolean canHost =!bot.canInteract(member);

        if(!canHost)
            Logger.guildWarn(guild,
                    member.getEffectiveName() + " tried to use host command. Need to revaluate permissions!");

        return canHost;
    }

    //If the user, member, is absolute power of the server or if the member's id is equal to the host's id
    public static boolean canModifyHost(Guild guild, Member member, SessionData sessionData) {
        boolean canModifyHost = sessionData.hostId.equals(member.getId()) || hasAbsolute(guild, member);

        if(!canModifyHost) {
            Logger.guildWarn(guild,
                    member.getEffectiveName() + " tried to use host command. Need to revaluate permissions!");
        }

        return canModifyHost;
    }

    //Returns true if member is the owner of the guild
    //If member has absolute power of the server
    public static boolean hasAbsolute(Guild guild, Member member) {
        return guild.getOwnerId().equals(member.getId());
    }

    public static boolean hasAbsolute(Guild guild, String id) { return guild.getOwnerId().equals(id); }

    public static CommandPrivilege[] getCommandPrivileges(Guild guild, String command) {
        List<Role> roles = guild.getRoles();

        CommandPrivilege[] privileges = new CommandPrivilege[roles.size() + 1];
        privileges[0] = new CommandPrivilege(USER, true, guild.retrieveOwner().complete().getIdLong());

        Member bot = guild.getMember(Main.getJda().getSelfUser());

        for(int i = 1; i < privileges.length; i++) {
            int j = i - 1;
            Role role = roles.get(j);

            boolean privilegeBool = true;
            if(BaseCommand.isHostingCommand(command))
                privilegeBool = !bot.canInteract(role);

            privileges[i] = new CommandPrivilege(ROLE, privilegeBool, role.getIdLong());
        }

        return privileges;
    }

}
