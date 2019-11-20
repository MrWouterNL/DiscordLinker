package nl.minetopiasdb.discordbot.discord.commands;

import java.awt.Color;
import java.util.UUID;

import nl.minetopiasdb.discordbot.utils.MessageUtils;
import nl.minetopiasdb.discordbot.utils.data.ConfigUtils;
import nl.minetopiasdb.discordbot.utils.link.DataLinkUtils;
import nl.minetopiasdb.discordbot.utils.link.LinkUtils;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class DiscordLinkCMD {

	@EventSubscriber
	public void onMessageRecive(MessageReceivedEvent e) {
		// Double spaces can cause issues.
		String msg = e.getMessage().getContent().replaceAll("  ", " ");
		if (msg.startsWith(ConfigUtils.getInstance().getPrefix() + "link")) {
			long id = e.getAuthor().getLongID();
			if (DataLinkUtils.getInstance().isLinked(id)) {
				MessageUtils.sendMessage(e.getChannel(),
						MessageUtils.getBuilder("Je hebt jouw Discord Account al gelinked!", Color.RED));
				return;
			}
			if (LinkUtils.getInstance().isValidLink(id)) {
				LinkUtils.getInstance().removeLink(id);
			}
			UUID linkUUID = LinkUtils.getInstance().registerLink(id);
			if (!MessageUtils.sendPrivateAndCheckIfCanReceive(e.getAuthor(),
					MessageUtils
							.getBuilder("Type het volgende commando in Minecraft om jouw account te linken: ```/link "
									+ linkUUID.toString() + "```", Color.GREEN))) {
				LinkUtils.getInstance().removeLink(linkUUID);
				MessageUtils.sendMessage(e.getChannel(), MessageUtils.getBuilder(
						e.getAuthor().mention() + ", je moet je priveberichten aan hebben staan!", Color.RED));
			}

		} else if (msg.startsWith(ConfigUtils.getInstance().getPrefix() + "unlink")) {
			long id = e.getAuthor().getLongID();
			LinkUtils.getInstance().removeLink(id);
			DataLinkUtils.getInstance().removeLink(id);
			MessageUtils.sendMessage(e.getChannel(), MessageUtils.getBuilder(
					e.getAuthor().mention() + ", succesvol jouw Minecraft account unlinked!", Color.GREEN));
		}
	}
}
