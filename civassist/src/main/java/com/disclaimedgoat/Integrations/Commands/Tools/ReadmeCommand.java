package com.disclaimedgoat.Integrations.Commands.Tools;

import com.disclaimedgoat.Integrations.Commands.BaseCommand;
import net.azzerial.slash.annotations.Slash;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@Slash.Tag("civreadme")
@Slash.Command(
        name = "civreadme",
        description = "Get important information regarding how to use this bot along with many common errors."
)
public final class ReadmeCommand extends BaseCommand {
    @Override
    public String getCommand() {
        return "civreadme";
    }

    @Override @Slash.Handler()
    public void execute(SlashCommandEvent event) {

    }

    @Override
    public String getUsage() {
        return "/civreadme";
    }
}
