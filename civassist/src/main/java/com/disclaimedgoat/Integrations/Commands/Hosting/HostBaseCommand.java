package com.disclaimedgoat.Integrations.Commands.Hosting;

import com.disclaimedgoat.Integrations.Commands.BaseCommand;
import com.disclaimedgoat.Utilities.Discord.EventUtils;
import com.disclaimedgoat.Utilities.Discord.PermissionUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;


public abstract class HostBaseCommand extends BaseCommand {

    protected boolean isIllegalHosting(SlashCommandEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        if(EventUtils.isNull(event, guild, member)) return true;

        //If the user cannot host, say so and return
        if(!PermissionUtil.canHost(guild, member)) {
            EventUtils.sendSilentReply(event, "🛑 You do not have the permission to create a new session!");
            return true;
        }
        return false;
    }

}
