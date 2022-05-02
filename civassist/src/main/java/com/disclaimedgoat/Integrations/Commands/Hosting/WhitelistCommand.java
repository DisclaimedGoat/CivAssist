package com.disclaimedgoat.Integrations.Commands.Hosting;

import net.azzerial.slash.annotations.Option;
import net.azzerial.slash.annotations.Slash;
import net.azzerial.slash.annotations.Subcommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import static net.azzerial.slash.annotations.OptionType.USER;

@Slash.Tag("civwhitelist")
@Slash.Command(
        name = "civwhitelist", description = "Whitelists ",
        subcommands = {
                @Subcommand(name = "add", description = "Adds only specific user(s) to a list to be able to join your sessions.",
                    options = {
                            @Option( name = "user", description = "Whitelist this user", type = USER, required = true),
                            @Option( name = "user1", description = "Whitelist this user", type = USER),
                            @Option( name = "user2", description = "Whitelist this user", type = USER),
                            @Option( name = "user3", description = "Whitelist this user", type = USER),
                            @Option( name = "user4", description = "Whitelist this user", type = USER),
                            @Option( name = "user5", description = "Whitelist this user", type = USER),
                            @Option( name = "user6", description = "Whitelist this user", type = USER),
                            @Option( name = "user7", description = "Whitelist this user", type = USER),
                            @Option( name = "user8", description = "Whitelist this user", type = USER),
                            @Option( name = "user9", description = "Whitelist this user", type = USER),
                            @Option( name = "user10", description = "Whitelist this user", type = USER),
                            @Option( name = "user11", description = "Whitelist this user", type = USER),
                            @Option( name = "user12", description = "Whitelist this user", type = USER),
                            @Option( name = "user13", description = "Whitelist this user", type = USER),
                            @Option( name = "user14", description = "Whitelist this user", type = USER)
                }),
                @Subcommand(name = "remove", description = "Removes specific user(s) to a list to be able to join your sessions.",
                    options = {
                            @Option( name = "user", description = "Whitelist this user", type = USER, required = true),
                            @Option( name = "user1", description = "Whitelist this user", type = USER),
                            @Option( name = "user2", description = "Whitelist this user", type = USER),
                            @Option( name = "user3", description = "Whitelist this user", type = USER),
                            @Option( name = "user4", description = "Whitelist this user", type = USER),
                            @Option( name = "user5", description = "Whitelist this user", type = USER),
                            @Option( name = "user6", description = "Whitelist this user", type = USER),
                            @Option( name = "user7", description = "Whitelist this user", type = USER),
                            @Option( name = "user8", description = "Whitelist this user", type = USER),
                            @Option( name = "user9", description = "Whitelist this user", type = USER),
                            @Option( name = "user10", description = "Whitelist this user", type = USER),
                            @Option( name = "user11", description = "Whitelist this user", type = USER),
                            @Option( name = "user12", description = "Whitelist this user", type = USER),
                            @Option( name = "user13", description = "Whitelist this user", type = USER),
                            @Option( name = "user14", description = "Whitelist this user", type = USER)
                    })
        }
)
public final class WhitelistCommand extends HostBaseCommand{

    @Override
    public String getCommand() {
        return "civwhitelist";
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
        return "/civwhitelist <add,remove> <user> (user...)";
    }
}
