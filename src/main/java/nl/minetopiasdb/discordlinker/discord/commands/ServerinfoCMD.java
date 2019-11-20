package nl.minetopiasdb.discordlinker.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import nl.minetopiasdb.discordlinker.utils.MessageUtils;
import nl.minetopiasdb.discordlinker.utils.commands.BotCommand;
import nl.minetopiasdb.discordlinker.utils.commands.Command;
import nl.minetopiasdb.discordlinker.utils.data.ConfigUtils;
import org.bukkit.Bukkit;

import java.awt.*;


public class ServerinfoCMD implements BotCommand {

    @Override
    public void excecute(Command cmd, String[] args, Message msg, MessageReceivedEvent event) {
        String ip = ConfigUtils.getInstance().getIP();
        EmbedBuilder embed = MessageUtils.getBuilder(Color.GREEN);
        embed.addField("IP", ip, false);
        embed.addField("Spelers", Bukkit.getOnlinePlayers().size() + " / " + Bukkit.getMaxPlayers(), false);
        event.getChannel().sendMessage(embed.build()).queue();
    }
}
