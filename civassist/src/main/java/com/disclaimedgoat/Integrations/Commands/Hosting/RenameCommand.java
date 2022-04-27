package com.disclaimedgoat.Integrations.Commands.Hosting;

import net.azzerial.slash.annotations.Option;
import net.azzerial.slash.annotations.Slash;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@Slash.Tag("civrename")
@Slash.Command(
        name = "civrename", description = "Renames the current session's name. Must be ran in a session channel you control.",
        options = {
                @Option(
                        name = "new_name",
                        description = "The name of the session. Must match EXACTLY with the session name in your Civilization game.",
                        type = net.azzerial.slash.annotations.OptionType.STRING,
                        required = true
                ),
        }
)
public final class RenameCommand extends HostBaseCommand {

    @Override
    public String getCommand() {
        return "civrename";
    }

    @Override @Slash.Handler()
    public void execute(SlashCommandEvent event) {
        //Checks if can use the host command
        if(isIllegalHosting(event)) return;
    }

    @Override
    public String getUsage() {
        return null;
    }
}
