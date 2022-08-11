package de.iv.jda.commands;

import de.iv.jda.core.Main;
import de.iv.jda.data.Assignment;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

public class AssignmentCommand extends ServerSlashCommand{

    @Override
    public String name() {
        return "assign";
    }

    @Override
    public String description() {
        return "Creates an assignment for a specific member.";
    }

    @Override
    public Permission permission() {
        return Permission.ADMINISTRATOR;
    }

    @Override
    public void execute(SlashCommandInteractionEvent e) {
        if(e.getOptions().size() == 1) {
            Member m = e.getOption("member").getAsMember();

            TextInput subject = TextInput.create("ass_title", "Title", TextInputStyle.SHORT)
                    .setPlaceholder("Specify a title for your assignment")
                    .setMinLength(5)
                    .setMaxLength(100) // or setRequiredRange(10, 100)
                    .build();

            TextInput body = TextInput.create("ass_msg", "Assignment", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Specify the task to assign")
                    .setMinLength(10)
                    .setMaxLength(1000)
                    .build();

            TextInput time = TextInput.create("ass_time", "Deadline", TextInputStyle.SHORT)
                    .setPlaceholder("i.e. 1h")
                    .setRequired(true)
                    .build();

            Modal modal = Modal.create("assignment", "Assignment")
                    .addActionRows(ActionRow.of(subject), ActionRow.of(body), ActionRow.of(time))
                    .build();

            Main.getRuntimeData(e.getMember()).setData("sender", e.getMember());
            Main.getRuntimeData(e.getMember()).setData("receiver", m);
            e.replyModal(modal).queue();

        } else if(e.getOptions().size() == 2) {
            Message.Attachment a = e.getOption("references").getAsAttachment();
            Member m = e.getOption("member").getAsMember();

            TextInput subject = TextInput.create("ass_title", "Title", TextInputStyle.SHORT)
                    .setPlaceholder("Specify a title for your assignment")
                    .setMinLength(5)
                    .setMaxLength(100) // or setRequiredRange(10, 100)
                    .setRequired(true)
                    .build();

            TextInput body = TextInput.create("ass_msg", "Assignment", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Specify the task to assign")
                    .setMinLength(10)
                    .setMaxLength(1000)
                    .setRequired(true)
                    .build();
            TextInput time = TextInput.create("ass_time", "Deadline", TextInputStyle.SHORT)
                    .setPlaceholder("i.e. 1h")
                    .setRequired(true)
                    .build();

            Modal modal = Modal.create("assignment", "Assignment")
                    .addActionRows(ActionRow.of(subject), ActionRow.of(body), ActionRow.of(time))
                    .build();
            e.replyModal(modal).queue();

            Main.getRuntimeData(e.getMember()).setData("sender", e.getMember());
            Main.getRuntimeData(e.getMember()).setData("receiver", m);
            Main.getRuntimeData(e.getMember()).setData("attachment", a);

        } else e.reply("Your command doesn't match any required type.").setEphemeral(true).queue();


        //e.reply("Created assignment " + m.getAsMention()).setEphemeral(false).queue();
    }
    @Override
    public void apply(Guild guild) {
        // /assign <user> *open form*
        guild.upsertCommand(name(), description())
                .addOption(OptionType.MENTIONABLE, "member", "The member the assignment is assigned to.", true)
                .addOption(OptionType.ATTACHMENT, "references", "You may add files to your assignment.", false)
                .queue();
    }
}
