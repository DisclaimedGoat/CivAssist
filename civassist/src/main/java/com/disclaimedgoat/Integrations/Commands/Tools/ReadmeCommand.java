package com.disclaimedgoat.Integrations.Commands.Tools;

import com.disclaimedgoat.Integrations.Commands.BaseCommand;
import com.disclaimedgoat.Utilities.DataManagement.FileManager;
import com.disclaimedgoat.Utilities.DataManagement.Logger;
import com.disclaimedgoat.Utilities.Discord.EmbedUtils;
import net.azzerial.slash.annotations.Slash;
import net.azzerial.slash.components.SlashButton;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static net.dv8tion.jda.api.entities.MessageEmbed.Field;

@Slash.Tag("civreadme")
@Slash.Command(
        name = "civreadme",
        description = "Get important information regarding how to use this bot along with many common errors."
)
public final class ReadmeCommand extends BaseCommand {

    private static EmbedBuilder[] pages;
    private static Map<String, Integer> userPageNumbers = new HashMap<>();

    public ReadmeCommand() {
        super();
        pages = buildPages();
    }

    @Override
    public String getCommand() {
        return "civreadme";
    }

    @Override @Slash.Handler()
    public void execute(SlashCommandEvent event) {
        String userId = event.getUser().getId();

        if(userPageNumbers.containsKey(userId))
            userPageNumbers.replace(userId, 0);
        else userPageNumbers.put(userId, 0);

        event.replyEmbeds(pages[0].build()).addActionRow(
                SlashButton.primary("page.back", "◀"),
                SlashButton.primary("page.forward", "▶")
        ).setEphemeral(true).queue();
    }

    @Slash.Button("page.forward")
    public void increasePage(ButtonClickEvent event) { changePage(event, 1); }

    @Slash.Button("page.back")
    public void decreasePage(ButtonClickEvent event) { changePage(event, -1); }

    private static void changePage(ButtonClickEvent event, int change) {
        String userId = event.getUser().getId();

        if(!userPageNumbers.containsKey(userId)) return;
        int page = userPageNumbers.get(userId) + change;
        page %= pages.length;

        event.editMessageEmbeds(pages[page].build()).queue();
    }


    @Override
    public String getUsage() {
        return "/civreadme";
    }

    private static EmbedBuilder[] buildPages() {

        FileManager embedFiles = new FileManager("civassist/src/main/resources/Readme Pages");
        File[] files = embedFiles.getChildrenAtRoot();

        ArrayList<EmbedBuilder> embeds = new ArrayList<>();

        for(int i = 0; i < files.length; i++) {
            File file = files[i];
            String filename = file.getName();
            if(!filename.startsWith("page") || !file.isFile()) continue;

            filename = filename.replace("page", "");
            int pageNumber;
            try { pageNumber = Integer.parseInt(filename); }
            catch(NumberFormatException e) {
                Logger.globalErrF("readme", "Could not parse %s into an integer!", filename);
                continue;
            }

            EmbedBuilder embed = EmbedUtils.buildClassic();

            try(Scanner scanner = new Scanner(file)) {
                String title = "";
                ArrayList<String> descriptions = new ArrayList<>();
                ArrayList<Field> fields = new ArrayList<>();

                String currentFieldTitle = "";
                ArrayList<String> currentFieldDescriptions = new ArrayList<>();

                while(scanner.hasNext()) {
                    String line = scanner.nextLine();
                    if(line.startsWith("#") || line.isEmpty()) continue;

                    line = line.replace(": ", ":");
                    int separator = line.indexOf(":");
                    if(separator == -1) continue;

                    String lineType = line.substring(0, separator);
                    String lineContent = line.substring(separator + 1);

                    switch(lineType) {
                        case "title":
                            title = lineContent;
                            break;
                        case "desc":
                            descriptions.add(lineContent);
                            break;
                        case "ftitle":
                            if(!currentFieldTitle.isEmpty()) {
                                String fieldDescription = "";
                                for(String s : currentFieldDescriptions) {
                                    fieldDescription += s + "\n";
                                }
                                fields.add(new Field(currentFieldTitle, fieldDescription, false));
                                currentFieldDescriptions.clear();
                            }
                            currentFieldTitle = lineContent;
                            break;
                        case "fdesc":
                            currentFieldDescriptions.add(lineContent);
                    }
                }

                if(!currentFieldTitle.isEmpty()) {
                    String fieldDescription = "";
                    for(String s : currentFieldDescriptions) {
                        fieldDescription += s + "\n";
                    }
                    fields.add(new Field(currentFieldTitle, fieldDescription, false));
                    currentFieldDescriptions.clear();
                }

                embed.setTitle(title);
                for(String s : descriptions)
                    embed.appendDescription(s + "\n");

                for(Field field : fields)
                    embed.addField(field);

            } catch (FileNotFoundException e) {
                embed.setTitle("Error!");
                embed.appendDescription("An error occurred when try to create readme data. Please alert server staff!");

                Logger.globalErr("readme", "File not found exception for readme command!", e.getLocalizedMessage());
            }

            embeds.add(pageNumber, embed);
        }

        return embeds.toArray(new EmbedBuilder[0]);
    }
}
