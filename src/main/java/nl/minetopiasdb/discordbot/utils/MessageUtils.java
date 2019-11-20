package nl.minetopiasdb.discordbot.utils;

import java.awt.Color;

import nl.minetopiasdb.discordbot.utils.data.ConfigUtils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

public class MessageUtils {

	public static EmbedBuilder getBuilder(String message, Color c) {
		return new EmbedBuilder().withAuthorIcon(ConfigUtils.getInstance().getLogo())
				.withAuthorName(ConfigUtils.getInstance().getHeader()).withFooterIcon(ConfigUtils.getInstance().getLogo())
				.withFooterText(ConfigUtils.getInstance().getFooter()).appendField("\nInformatie:", "\n" + message, true)
				.withColor(c);
	}

	public static boolean sendPrivateAndCheckIfCanReceive(IUser user, EmbedBuilder builder) {
		return RequestBuffer.request(() -> {
			try {
				user.getOrCreatePMChannel().sendMessage(builder.build()).getLongID();
			} catch (Exception ex) {
				if (ex.getMessage().contains("Received 403 forbidden error for url") || ex.getMessage()
						.contains("Message was unable to be sent (Discord didn't return a response).")) {
					return false;
				}
			}
			return true;
		}).get();
	}
	
	public static void sendMessage(IChannel channel, EmbedBuilder builder) {
		RequestBuffer.request(() -> {
			channel.sendMessage(builder.build());
		});
	}

	public static void sendMessage(IChannel channel, String msg) {
		RequestBuffer.request(() -> {
			channel.sendMessage(msg);
		});
	}

	public static void deleteMessage(IUser usr, Long id) {
		RequestBuffer.request(() -> {
			usr.getOrCreatePMChannel().getMessageByID(id).delete();
		});
	}
}
