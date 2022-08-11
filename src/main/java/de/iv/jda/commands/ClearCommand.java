package de.iv.jda.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Collection;
import java.util.List;

public class ClearCommand extends ServerSlashCommand {

    @Override
    public String name() {
        return "clear";
    }

    @Override
    public String description() {
        return "Deletes a specified amount of chat messages";
    }

    @Override
    public Permission permission() {
        return Permission.ADMINISTRATOR;
    }

    @Override
    public void execute(SlashCommandInteractionEvent e) {
        int amount = e.getOption("amount").getAsInt();
        List<Message> messageList = e.getChannel().getHistory().retrievePast(amount).complete();
        if(messageList.size() >= amount) {
            e.getChannel().asTextChannel().deleteMessages(messageList).queue();
            e.reply("`Deleted " + e.getOption("amount").getAsInt() + " messages`").setEphemeral(true).queue();
        } else e.reply("`"+amount+" is too big. Choose a smaller amount.`").queue();

    }

    @Override
    public void apply(Guild guild) {
        guild.upsertCommand(name(), description())
                .addOption(OptionType.INTEGER, "amount", "The amount of messages to be deleted.", true)
                .queue();
    }
}
