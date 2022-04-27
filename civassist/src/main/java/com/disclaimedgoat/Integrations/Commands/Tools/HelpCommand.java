package com.disclaimedgoat.Integrations.Commands.Tools;

import com.disclaimedgoat.Integrations.Commands.BaseCommand;
import net.azzerial.slash.annotations.Choice;
import net.azzerial.slash.annotations.Option;
import net.azzerial.slash.annotations.OptionType;
import net.azzerial.slash.annotations.Slash;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@Slash.Tag("civhelp")
@Slash.Command(
        name = "civhelp", description = "Get started or gain more information for a specific command!",
        options = {
                @Option(
                        name = "command",
                        description = "The name of the command you seek more information on.",
                        type = OptionType.STRING,
                        choices = {
                                @Choice(name = "civcreate", value = "civcreate"),
                                @Choice(name = "civdisband", value = "civdisband"),
                                @Choice(name = "civkick", value = "civkick"),
                                @Choice(name = "civrename", value = "civrename"),
                                @Choice(name = "civblacklist", value = "civblacklist"),
                                @Choice(name = "civarchive", value = "civarchive"),

                                @Choice(name = "civjoin", value = "civjoin"),
                                @Choice(name = "civleave", value = "civleave"),

                                @Choice(name = "civhelp", value = "civhelp"),
                                @Choice(name = "civlist", value = "civlist"),
                                @Choice(name = "civreadme", value = "civreadme"),
                                @Choice(name = "civreg", value = "civreg")
                        }
                )
        }
)
public final class HelpCommand extends BaseCommand {
    @Override
    public String getCommand() {
        return "civhelp";
    }

    @Override @Slash.Handler()
    public void execute(SlashCommandEvent event) {

    }

    @Override
    public String getUsage() {
        return "/civhelp [command]";
    }
}
