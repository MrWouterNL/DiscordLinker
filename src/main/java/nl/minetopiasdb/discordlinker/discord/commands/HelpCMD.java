package nl.minetopiasdb.discordlinker.discord.commands;

import java.awt.Color;

import nl.minetopiasdb.discordlinker.utils.MessageUtils;
import nl.minetopiasdb.discordlinker.utils.data.ConfigUtils;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

public class HelpCMD {
	
	@EventSubscriber
	public void onMessageRecive(MessageReceivedEvent e) {
		String msg = e.getMessage().getContent().replaceAll("  ", " ");
		if (msg.startsWith(ConfigUtils.getInstance().getPrefix() + "help")) {
			String prefix = ConfigUtils.getInstance().getPrefix();
			
			EmbedBuilder embed = new EmbedBuilder().withAuthorIcon(ConfigUtils.getInstance().getLogo())
					.withAuthorName(ConfigUtils.getInstance().getHeader())
					.withFooterIcon(ConfigUtils.getInstance().getLogo())
					.withFooterText(ConfigUtils.getInstance().getFooter()).withColor(Color.GREEN);

			
			embed.appendField(prefix + "help", 
					"De pagina die jij nu bekijkt", false);
			embed.appendField(prefix + "link", 
					"Link jouw Minecraft account met jouw Discord Account.", false);
			embed.appendField(prefix + "unlink", 
					"Unlink jouw Minecraft account.", false);
			embed.appendField(prefix + "server", 
					"Bekijk informatie over de server.", false);
			embed.appendField(prefix + "stats", 
					"Bekijk jouw MinetopiaSDB informatie.", false);
			
			MessageUtils.sendMessage(e.getChannel(), embed);
		}
	}
}
