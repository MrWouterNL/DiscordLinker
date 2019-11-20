package nl.minetopiasdb.discordlinker.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import nl.minetopiasdb.discordlinker.utils.MessageUtils;
import nl.minetopiasdb.discordlinker.utils.commands.BotCommand;
import nl.minetopiasdb.discordlinker.utils.commands.Command;
import nl.minetopiasdb.discordlinker.utils.data.ConfigUtils;

import java.awt.*;

public class HelpCMD implements BotCommand {

    @Override
    public void excecute(Command cmd, String[] args, Message msg, MessageReceivedEvent event) {
        String prefix = ConfigUtils.getInstance().getPrefix();

        EmbedBuilder embed = MessageUtils.getBuilder(Color.GREEN);

        embed.addField(prefix + "help",
                "De pagina die jij nu bekijkt", false);
        embed.addField(prefix + "link",
                "Link jouw Minecraft account met jouw Discord Account.", false);
        embed.addField(prefix + "unlink",
                "Unlink jouw Minecraft account.", false);
        embed.addField(prefix + "server",
                "Bekijk informatie over de server.", false);
        embed.addField(prefix + "stats",
                "Bekijk jouw MinetopiaSDB informatie.", false);

        event.getChannel().sendMessage(embed.build()).queue();
    }
}
