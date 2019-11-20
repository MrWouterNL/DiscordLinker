package nl.minetopiasdb.discordlinker.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import nl.minetopiasdb.discordlinker.utils.MessageUtils;
import nl.minetopiasdb.discordlinker.utils.commands.BotCommand;
import nl.minetopiasdb.discordlinker.utils.commands.Command;
import nl.minetopiasdb.discordlinker.utils.link.DataLinkUtils;
import nl.minetopiasdb.discordlinker.utils.link.LinkUtils;

import java.awt.*;

public class DiscordUnlinkCMD implements BotCommand {
    @Override
    public void excecute(Command cmd, String[] args, Message msg, MessageReceivedEvent event) {
        long id = event.getAuthor().getIdLong();
        LinkUtils.getInstance().removeLink(id);
        DataLinkUtils.getInstance().removeLink(id);
        EmbedBuilder embed = MessageUtils.getBuilder(Color.GREEN)
                .setDescription(event.getAuthor().getAsTag() + ", je Discord account is succesvol geunlinked");
        event.getChannel().sendMessage(embed.build()).queue();
    }
}
