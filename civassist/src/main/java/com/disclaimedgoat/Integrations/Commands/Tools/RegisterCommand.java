package com.disclaimedgoat.Integrations.Commands.Tools;

import com.disclaimedgoat.Integrations.Commands.BaseCommand;
import com.disclaimedgoat.Integrations.Data.UserData;
import com.disclaimedgoat.Utilities.DataManagement.Logger;
import com.disclaimedgoat.Utilities.Discord.EventUtils;
import net.azzerial.slash.annotations.Option;
import net.azzerial.slash.annotations.Slash;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

@Slash.Tag("civreg")
@Slash.Command(
        name = "civreg", description = "Registers your Civilization 6 username to Discord. Required to join a session.",
        options = {
                @Option(
                        name = "name",
                        description = "Your Civilization 6 name.",
                        type = net.azzerial.slash.annotations.OptionType.STRING,
                        required = true
                ),
        }
)
public final class RegisterCommand extends BaseCommand {

    @Override
    public String getCommand() {
        return "civreg";
    }

    @Override @Slash.Handler()
    public void execute(SlashCommandEvent event) {

        OptionMapping playerNameMapping = event.getOption("name");
        if(EventUtils.isNull(event, playerNameMapping)) return;

        String playerName = playerNameMapping.getAsString();

        UserData data = UserData.getUser(event.getUser().getId());
        data.setValue("playerName", playerName);

        EventUtils.sendSilentReply(event, "Successfully registered your game name as `" + playerName + "`");

        Logger.globalLog("registry", "User %s registered civ6 name to '%s'",
                event.getUser().getName(), playerName);
    }

    @Override
    public String getUsage() {
        return "/civreg <name>";
    }
}
