package nl.minetopiasdb.discordbot.discord.commands;

import java.awt.Color;

import org.bukkit.Bukkit;

import nl.minetopiasdb.discordbot.utils.MessageUtils;
import nl.minetopiasdb.discordbot.utils.data.ConfigUtils;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

public class ServerinfoCMD {
	
	@EventSubscriber
	public void onMessageRecive(MessageReceivedEvent e) {
		String msg = e.getMessage().getContent().replaceAll("  ", " ");
		if (msg.startsWith(ConfigUtils.getInstance().getPrefix() + "server")) {
			String ip = ConfigUtils.getInstance().getIP();
			
			EmbedBuilder embed = new EmbedBuilder().withAuthorIcon(ConfigUtils.getInstance().getLogo())
					.withAuthorName(ConfigUtils.getInstance().getHeader())
					.withFooterIcon(ConfigUtils.getInstance().getLogo())
					.withFooterText(ConfigUtils.getInstance().getFooter()).withColor(Color.GREEN);

			embed.appendField("IP", ip, false);
			embed.appendField("Spelers", Bukkit.getOnlinePlayers().size() + " / " + Bukkit.getMaxPlayers(), false);
			MessageUtils.sendMessage(e.getChannel(), embed);
		}
	}
}
