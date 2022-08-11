package de.iv.jda.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public abstract class ServerSlashCommand {

    public abstract String name();
    public abstract String description();
    public abstract Permission permission();
    public abstract void execute(SlashCommandInteractionEvent e);

    public void apply(Guild guild) {
        guild.upsertCommand(name(), description()).queue();
    }

}
