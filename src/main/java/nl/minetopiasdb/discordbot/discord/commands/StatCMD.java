package nl.minetopiasdb.discordbot.discord.commands;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import nl.minetopiasdb.api.API;
import nl.minetopiasdb.api.SDBPlayer;
import nl.minetopiasdb.api.enums.TimeType;
import nl.minetopiasdb.discordbot.Main;
import nl.minetopiasdb.discordbot.utils.MessageUtils;
import nl.minetopiasdb.discordbot.utils.data.ConfigUtils;
import nl.minetopiasdb.discordbot.utils.data.ConfigUtils.ShowOption;
import nl.minetopiasdb.discordbot.utils.link.DataLinkUtils;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;

public class StatCMD {

	@EventSubscriber
	public void onMessageRecive(MessageReceivedEvent e) {
		String msg = e.getMessage().getContent().replaceAll("  ", " ");
		if (msg.startsWith(ConfigUtils.getInstance().getPrefix() + "stats")) {
			long userId = e.getAuthor().getLongID();
			if (e.getMessage().getMentions().size() > 0
					&& e.getAuthor().getPermissionsForGuild(e.getGuild()).contains(Permissions.ADMINISTRATOR)) {
				userId = e.getMessage().getMentions().get(0).getLongID();
			}
			if (!DataLinkUtils.getInstance().isLinked(userId)) {
				if (userId == e.getAuthor().getLongID()) {
					MessageUtils.sendMessage(e.getChannel(), MessageUtils.getBuilder(
							e.getAuthor().mention() + ", je hebt jouw discord account niet gelinked!", Color.RED));
				} else {
					MessageUtils.sendMessage(e.getChannel(),
							MessageUtils.getBuilder(Main.getBot().getUserByID(userId).getName() + "#"
									+ Main.getBot().getUserByID(userId).getDiscriminator()
									+ " heeft zijn discord account niet gelinked!", Color.RED));
				}

				return;
			}
			OfflinePlayer p = Bukkit.getOfflinePlayer(DataLinkUtils.getInstance().getUUIDFromDiscord(userId));
			SDBPlayer sdb = SDBPlayer.createSDBPlayer(p);
			Plugin pl = Bukkit.getPluginManager().getPlugin("MinetopiaSDB");

			EmbedBuilder builder = new EmbedBuilder().withAuthorIcon(ConfigUtils.getInstance().getLogo())
					.withAuthorName(ConfigUtils.getInstance().getHeader())
					.withFooterIcon(ConfigUtils.getInstance().getLogo())
					.withFooterText(ConfigUtils.getInstance().getFooter()).withColor(Color.GREEN);
			builder.appendField("Speler", p.getName(), false);

			ConfigUtils cu = ConfigUtils.getInstance();
			
			if (cu.getShowOption(ShowOption.FITHEID)) {
				builder.appendField(cu.getShowTitle(ShowOption.FITHEID), sdb.getFitheid() + "/" + pl.getConfig().getString("Fitheid.Max"), false);
			}
			if (cu.getShowOption(ShowOption.PREFIX)) {
				builder.appendField(cu.getShowTitle(ShowOption.PREFIX), sdb.getPrefix(), false);
			}
			if (cu.getShowOption(ShowOption.RANK)) {
				builder.appendField(cu.getShowTitle(ShowOption.RANK), sdb.getRank(), false);
			}
			if (cu.getShowOption(ShowOption.LEVEL)) {
				builder.appendField(cu.getShowTitle(ShowOption.LEVEL), "" + sdb.getLevel(), false);
			}
			if (cu.getShowOption(ShowOption.MONEY)) {
				builder.appendField(cu.getShowTitle(ShowOption.MONEY), "€ " + format(API.getEcon().getBalance(p)), false);
			}
			if (cu.getShowOption(ShowOption.ONLINETIME)) {
				builder.appendField(cu.getShowTitle(ShowOption.ONLINETIME), "" + sdb.getTime(TimeType.DAYS) + " dagen, "
						+ sdb.getTime(TimeType.HOURS) + " uur, " + sdb.getTime(TimeType.MINUTES) + " minuten", false);
			}
			if (cu.getShowOption(ShowOption.ONLINESTATUS)) {
				builder.appendField(cu.getShowTitle(ShowOption.ONLINESTATUS), p.isOnline() ? "Ja" : "Nee", false);
			}
			if (cu.getShowOption(ShowOption.GRAYCOIN)) {
				builder.appendField(cu.getShowTitle(ShowOption.GRAYCOIN), "" + sdb.getGrayCoins(), false);
			}
			MessageUtils.sendMessage(e.getChannel(), builder);
		}
	}

	public static String format(double number) {
		DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.GERMAN);
		return df.format(number);
	}
}
