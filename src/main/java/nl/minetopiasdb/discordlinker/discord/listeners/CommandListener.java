package nl.minetopiasdb.discordlinker.discord.listeners;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nl.minetopiasdb.discordlinker.utils.commands.CommandFactory;
import nl.minetopiasdb.discordlinker.utils.data.ConfigUtils;

public class CommandListener extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        if (event.isFromType(ChannelType.TEXT)) {
            if (event.getMessage().getContentDisplay().startsWith(ConfigUtils.getInstance().getPrefix()) && event.getMessage().getAuthor().getId() != event.getJDA().getSelfUser().getId()) {
                CommandFactory.getInstance().excecute(event.getMessage().getContentDisplay(), event.getMessage(), event);
            }
        }
    }
}
