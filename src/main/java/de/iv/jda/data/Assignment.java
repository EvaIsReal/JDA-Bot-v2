package de.iv.jda.data;

import de.iv.jda.core.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class Assignment implements Runnable, Serializable {

    private long senderId, receiverID;
    private String title, message;
    private Date dateCreated, dateExpired;

    private File attachment;

    public long getReceiverID() {
        return receiverID;
    }


    public Assignment(long sender, long receiver) {
        this.senderId = sender;
        this.receiverID = receiver;
        this.dateCreated = new Date(System.currentTimeMillis());
        this.title = "";
        this.message = "";
    }

    public Date getDateExpired() {
        return dateExpired;
    }

    public void setDateExpired(Date dateExpired) {
        this.dateExpired = dateExpired;
    }

    public void setSender(long sender) {
        this.senderId = sender;
    }

    public void setReceiverID(long receiverID) {
        this.receiverID = receiverID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setAttachment(File attachment) {
        this.attachment = attachment;
    }

    public long getSenderId() {
        return senderId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public File getAttachment() {
        return attachment;
    }

    public Member getSender(long id) {
        return Main.getGuild().getMemberById(senderId);
    }

    public Member getReceiver(long id) {
        return Main.getGuild().getMemberById(receiverID);
    }

    @Override
    public void run() {
        if (this.getDateExpired().getTime() < System.currentTimeMillis()) {
            Main.assignments.remove(this);
            Member receiver = getReceiver(getReceiverID());
            String senderName = Main.getJda().retrieveUserById(senderId).complete().getName();
            User user = Main.getJda().retrieveUserById(receiverID).complete();
            user.openPrivateChannel().queue(c -> {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Your Assignment's deadline is overdue");
                eb.addField(":notebook_with_decorative_cover: Task", this.getMessage(), false);
                eb.setColor(new Color(150, 9, 162));
                eb.setThumbnail("https://th.bing.com/th/id/R.53b73928ee798a0f077007b42bc64c33?rik=jfRnqNwcUisZCg&riu=http%3a%2f%2fpngimg.com%2fuploads%2fexclamation_mark%2fexclamation_mark_PNG35.png&ehk=9jxBYLg%2b6qz0ARshPd3q4lxEZG2FeIC%2b0reXp1PKsXo%3d&risl=&pid=ImgRaw&r=0");
                eb.setFooter("This assignment was created by " + senderName, Main.getGuild().getIconUrl());
                c.sendMessageEmbeds(eb.build()).queue();
            });
        }
    }
}
