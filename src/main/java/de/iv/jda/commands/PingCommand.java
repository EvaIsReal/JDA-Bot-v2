package de.iv.jda.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PingCommand extends ServerSlashCommand {

    @Override
    public String name() {
        return "ping";
    }

    @Override
    public String description() {
        return "Pings the Server and returns latency-time";
    }

    @Override
    public Permission permission() {
        return Permission.MESSAGE_SEND;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        long time = System.currentTimeMillis();
        event.reply("Pong!").setEphemeral(true) // reply or acknowledge
                .flatMap(v ->
                        event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time) // then edit original
                ).queue();
    }
}
