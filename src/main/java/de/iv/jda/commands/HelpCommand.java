package de.iv.jda.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class HelpCommand extends ServerSlashCommand {

    @Override
    public String name() {
        return "help";
    }

    @Override
    public String description() {
        return "Gives some useful information.";
    }

    @Override
    public Permission permission() {
        return Permission.MESSAGE_SEND;
    }

    @Override
    public void execute(SlashCommandInteractionEvent e) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Command Help")
                .setThumbnail("https://clipartcraft.com/images/question-mark-transparent-red.png");


        switch (e.getOption("command").getAsString()) {
            case "clear" -> {

            }
        }
    }

    @Override
    public void apply(Guild guild) {
        guild.upsertCommand(name(), description())
                .addOption(OptionType.STRING, "command", "Get help for this command").queue();
    }
}
