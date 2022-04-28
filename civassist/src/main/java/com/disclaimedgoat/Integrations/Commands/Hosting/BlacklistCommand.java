package com.disclaimedgoat.Integrations.Commands.Hosting;

import net.azzerial.slash.annotations.Option;
import net.azzerial.slash.annotations.Slash;
import net.azzerial.slash.annotations.Subcommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import static net.azzerial.slash.annotations.OptionType.USER;

@Slash.Tag("civblacklist")
@Slash.Command(
        name = "civblacklist", description = "Blacklists a user from a ever joining one of your sessions. Only affects current session channel.",
        subcommands = {
                @Subcommand(name = "add", description = "Adds user(s) to a list to blacklist from your sessions",
                    options = {
                            @Option( name = "user", description = "Blacklist this user", type = USER, required = true),
                            @Option( name = "user1", description = "Blacklist this user", type = USER),
                            @Option( name = "user2", description = "Blacklist this user", type = USER),
                            @Option( name = "user3", description = "Blacklist this user", type = USER),
                            @Option( name = "user4", description = "Blacklist this user", type = USER),
                            @Option( name = "user5", description = "Blacklist this user", type = USER),
                            @Option( name = "user6", description = "Blacklist this user", type = USER),
                            @Option( name = "user7", description = "Blacklist this user", type = USER),
                            @Option( name = "user8", description = "Blacklist this user", type = USER),
                            @Option( name = "user9", description = "Blacklist this user", type = USER),
                            @Option( name = "user10", description = "Blacklist this user", type = USER),
                            @Option( name = "user11", description = "Blacklist this user", type = USER),
                            @Option( name = "user12", description = "Blacklist this user", type = USER),
                            @Option( name = "user13", description = "Blacklist this user", type = USER),
                            @Option( name = "user14", description = "Blacklist this user", type = USER)
                }),
                @Subcommand(name = "remove", description = "Removes user(s) to a list to blacklist from your sessions",
                    options = {
                            @Option( name = "user", description = "Blacklist this user", type = USER, required = true),
                            @Option( name = "user1", description = "Blacklist this user", type = USER),
                            @Option( name = "user2", description = "Blacklist this user", type = USER),
                            @Option( name = "user3", description = "Blacklist this user", type = USER),
                            @Option( name = "user4", description = "Blacklist this user", type = USER),
                            @Option( name = "user5", description = "Blacklist this user", type = USER),
                            @Option( name = "user6", description = "Blacklist this user", type = USER),
                            @Option( name = "user7", description = "Blacklist this user", type = USER),
                            @Option( name = "user8", description = "Blacklist this user", type = USER),
                            @Option( name = "user9", description = "Blacklist this user", type = USER),
                            @Option( name = "user10", description = "Blacklist this user", type = USER),
                            @Option( name = "user11", description = "Blacklist this user", type = USER),
                            @Option( name = "user12", description = "Blacklist this user", type = USER),
                            @Option( name = "user13", description = "Blacklist this user", type = USER),
                            @Option( name = "user14", description = "Blacklist this user", type = USER)
                    })
        }
)
public final class BlacklistCommand extends HostBaseCommand{

    @Override
    public String getCommand() {
        return "civblacklist";
    }

    @Override @Slash.Handler("add") //Executes for the add subcommand
    public void execute(SlashCommandEvent event) {
        //Checks if can use the host command
        if(isIllegalHosting(event)) return;

    }

    @Slash.Handler("remove") //Executes for the add subcommand
    public void executeRemove(SlashCommandEvent event) {
        //Checks if can use the host command
        if(isIllegalHosting(event)) return;
    }

    @Override
    public String getUsage() {
        return "/civblacklist <add,remove> <user> (user...)";
    }
}
