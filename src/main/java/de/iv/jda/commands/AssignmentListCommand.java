package de.iv.jda.commands;

import de.iv.jda.core.Main;
import de.iv.jda.data.Assignment;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.stream.Collectors;

public class AssignmentListCommand extends ServerSlashCommand {

    @Override
    public String name() {
        return "assignments";
    }

    @Override
    public String description() {
        return "Lists all active assignments given to you.";
    }

    @Override
    public Permission permission() {
        return Permission.ADMINISTRATOR;
    }

    @Override
    public void execute(SlashCommandInteractionEvent e) {
        List<Assignment> assignmentList = Main.assignments.stream()
                .filter(a -> a.getReceiverID() == e.getMember().getIdLong())
                .toList();
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.MEDIUM);

        EmbedBuilder eb = new EmbedBuilder()
                .setTitle("Your Assignments")
                .setThumbnail("https://cdn3.iconfinder.com/data/icons/planning/100/Planning_colour5-512.png")
                .setColor(new Color(150, 9, 162))
                .setFooter("Requested at " + date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
                        .toString(), e.getGuild().getIconUrl());

        for (int i = 0; i < assignmentList.size(); i++) {
            eb.addField(":notebook_with_decorative_cover: Task", assignmentList.get(i).getMessage(), true);
            eb.addBlankField(true);
            eb.addField(":alarm_clock: Deadline", assignmentList.get(i).getDateExpired().toString(), true);
            eb.addBlankField(false);
        }

        e.replyEmbeds(eb.build()).queue();
    }
}
