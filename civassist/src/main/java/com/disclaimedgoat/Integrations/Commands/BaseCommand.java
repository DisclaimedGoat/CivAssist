package com.disclaimedgoat.Integrations.Commands;

import com.disclaimedgoat.Integrations.Commands.Hosting.*;
import com.disclaimedgoat.Integrations.Commands.Joining.JoinCommand;
import com.disclaimedgoat.Integrations.Commands.Joining.LeaveCommand;
import com.disclaimedgoat.Integrations.Commands.Tools.HelpCommand;
import com.disclaimedgoat.Integrations.Commands.Tools.ListCommand;
import com.disclaimedgoat.Integrations.Commands.Tools.ReadmeCommand;
import com.disclaimedgoat.Integrations.Commands.Tools.RegisterCommand;
import com.disclaimedgoat.Main;
import com.disclaimedgoat.Utilities.SlashCommands;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseCommand {
    private static final Map<String, BaseCommand> commands = new HashMap<>();

    /**
     * Initialize all of the commands
     */
    public static void init() {

        SlashCommands.init(Main.getJda());

        //Hosting Commands
        new ArchiveCommand();
        new BlacklistCommand();
        new CreateCommand();
        new DisbandCommand();
        new KickCommand();
        new RenameCommand();
        new WhitelistCommand();

        //Joining commands
        new JoinCommand();
        new LeaveCommand();

        //Tool Commands
        new RegisterCommand();
        new ListCommand();
        new ReadmeCommand();

        //Have help be last
        new HelpCommand();

        for(BaseCommand command : commands.values())
            SlashCommands.addCommand(command);

        SlashCommands.buildCommands();

        for(String commandStr : getCommands())
            SlashCommands.addCommandToGuilds(commandStr);

        SlashCommands.updatePrivileges();
    }

    /**
     * Simple constructor for the subclasses
     */
    protected BaseCommand() { commands.put(getCommand(), this); }


    //Abstract methods that have to be inherited by the other commands.//
    // -------------------------------------------------------------- //

    public abstract void execute(SlashCommandEvent event);

    public abstract String getCommand();

    public abstract String getUsage();

    public static String[] getCommands() { return commands.keySet().toArray(new String[0]); }

    public static boolean isHostingCommand(String commandName) {
        BaseCommand command = commands.get(commandName);
        return command instanceof HostBaseCommand;
    }
}
