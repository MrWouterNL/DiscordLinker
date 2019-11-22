package nl.minetopiasdb.discordlinker.utils.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface BotCommand {

    public void excecute(Command cmd, String[] args, Message msg, MessageReceivedEvent event);

}
