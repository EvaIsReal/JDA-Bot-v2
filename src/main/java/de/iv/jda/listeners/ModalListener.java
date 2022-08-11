package de.iv.jda.listeners;

import de.iv.jda.core.Main;
import de.iv.jda.core.Uni;
import de.iv.jda.data.Assignment;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.AttachmentOption;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class ModalListener extends ListenerAdapter {

    public void onModalInteraction(ModalInteractionEvent event) {
        LocalDateTime date = LocalDateTime.now();
        if (event.getModalId().equals("assignment")) {

            String title = event.getValue("ass_title").getAsString();
            String msg = event.getValue("ass_msg").getAsString();
            String timeString = event.getValue("ass_time").getAsString();

            Assignment assignment;

            Member sender = (Member) Main.getRuntimeData(event.getMember()).getData("sender");
            Member receiver = (Member) Main.getRuntimeData(event.getMember()).getData("receiver");
            Message.Attachment attachment = (Message.Attachment) Main.getRuntimeData(event.getMember()).getData("attachment");

            Date dateCreated = new Date(System.currentTimeMillis());
            long creationMillis = dateCreated.getTime();
            long additionMillis = Uni.getMillisFromString(timeString);
            Date dateExpired = new Date(creationMillis + additionMillis);
            DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            String expirationString = format.format(dateExpired) + " Uhr";

            assignment = Uni.createAssignment(sender, receiver, title, msg, dateExpired);

            if(assignment.getAttachment() != null) {
                //A file is attached to the assignment
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(new Color(150, 9, 162));
                eb.setThumbnail("https://images-ext-1.discordapp.net/external/pZTkQHqZatGmj0ju7-ezQ8QT-NwrMTpcm5r1s7Arl-c/https/cdn-icons-png.flaticon.com/512/2038/2038022.png");
                eb.setTitle("Assignment");
                eb.setDescription("New assignment from " + sender.getEffectiveName() + " for " + receiver.getAsMention());
                eb.addField(":notebook_with_decorative_cover: Task", msg, false);
                eb.addField(":alarm_clock: Deadline", expirationString, false);
                eb.setFooter("Created at " + date
                        .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
                        .toString(), event.getGuild().getIconUrl());

                event.getChannel().sendMessageEmbeds(eb.build()).queue();
                Uni.getAssignments().add(assignment);
                try {
                    event.replyFile(attachment.downloadToFile().get(), AttachmentOption.SPOILER).queue();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            } else {

                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(new Color(150, 9, 162));
                eb.setThumbnail("https://images-ext-1.discordapp.net/external/pZTkQHqZatGmj0ju7-ezQ8QT-NwrMTpcm5r1s7Arl-c/https/cdn-icons-png.flaticon.com/512/2038/2038022.png");
                eb.setTitle("Assignment");
                eb.setDescription("New assignment from " + sender.getEffectiveName() + " for " + receiver.getAsMention());
                eb.addField(":notebook_with_decorative_cover: Task", msg, false);
                eb.addField(":alarm_clock: Deadline", expirationString, false);
                eb.setFooter("Created at " + date
                        .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
                        .toString(), event.getGuild().getIconUrl());

                event.replyEmbeds(eb.build()).queue();
                Main.assignments.add(assignment);
            }
        }
    }


}
