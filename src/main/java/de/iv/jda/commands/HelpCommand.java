package de.iv.jda.commands;

import de.iv.jda.listeners.SlashCommandManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;

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
        LocalDateTime date = LocalDateTime.now();

        eb.setTitle("Command Help")
                .setThumbnail("https://clipartcraft.com/images/question-mark-transparent-red.png")
                .setFooter("Requested at " + date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.GERMANY))
                        .toString(), e.getGuild().getIconUrl());
        eb.setColor(new Color(150, 9, 162));

       if(e.getOption("command").getAsString() != null) {
           switch (e.getOption("command").getAsString()) {
               case "clear" -> {
                   eb.addField("/clear <int n>", "Clears n messages in current text channel", false);
                   e.replyEmbeds(eb.build()).queue();
               }
               case "assign" -> {
                   eb.addField("/assign <Member member>", "Assigns an _assignment_ to _member_.", false);
                   e.replyEmbeds(eb.build()).queue();
               }
               case "assignments" -> {
                   eb.addField("/assignments", "Lists every assignment assigned to the sender", false);
                   e.replyEmbeds(eb.build()).queue();
               }
               case "ping" -> {
                   eb.addField("/ping", "Pings the server and returns latency time", false);
                   e.replyEmbeds(eb.build()).queue();
               }
               default -> {
                   e.reply("Your command doesn't match any required type.").setEphemeral(true).queue();
               }
           }
       } else {
           SlashCommandManager.getServerSlashCommandList().forEach(c -> {
               eb.addField(c.name(), c.description(), false);
               eb.addBlankField(false);
               e.replyEmbeds(eb.build()).queue();
           });
       }


    }

    @Override
    public void apply(Guild guild) {
        guild.upsertCommand(name(), description())
                .addOption(OptionType.STRING, "command", "Get help for this command").queue();
    }
}
