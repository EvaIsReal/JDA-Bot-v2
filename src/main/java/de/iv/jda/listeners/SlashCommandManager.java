package de.iv.jda.listeners;

import de.iv.jda.commands.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SlashCommandManager extends ListenerAdapter {

    private static List<ServerSlashCommand> serverSlashCommandList = new ArrayList<>();

    public SlashCommandManager() {
        serverSlashCommandList.add(new PingCommand());
        serverSlashCommandList.add(new AssignmentCommand());
        serverSlashCommandList.add(new ClearCommand());
        serverSlashCommandList.add(new AssignmentListCommand());
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        for (ServerSlashCommand command : serverSlashCommandList) {
            if(e.getName().equals(command.name())) {
                if(e.getMember().hasPermission(command.permission())) command.execute(e);
                else e.reply("Sorry, but you don't have the permission to execute this command.").setEphemeral(true).queue();
            }
        }
    }

    public static List<ServerSlashCommand> getServerSlashCommandList() {
        return serverSlashCommandList;
    }
}
