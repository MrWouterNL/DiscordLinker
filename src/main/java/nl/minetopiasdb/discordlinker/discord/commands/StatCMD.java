package nl.minetopiasdb.discordlinker.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import nl.minetopiasdb.api.API;
import nl.minetopiasdb.api.SDBPlayer;
import nl.minetopiasdb.api.enums.TimeType;
import nl.minetopiasdb.discordlinker.Main;
import nl.minetopiasdb.discordlinker.utils.MessageUtils;
import nl.minetopiasdb.discordlinker.utils.commands.BotCommand;
import nl.minetopiasdb.discordlinker.utils.commands.Command;
import nl.minetopiasdb.discordlinker.utils.data.ConfigUtils;
import nl.minetopiasdb.discordlinker.utils.data.ConfigUtils.ShowOption;
import nl.minetopiasdb.discordlinker.utils.link.DataLinkUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class StatCMD implements BotCommand {

    public static String format(double number) {
        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.GERMAN);
        return df.format(number);
    }

    @Override
    public void excecute(Command cmd, String[] args, Message msg, MessageReceivedEvent event) {
        long userId = event.getAuthor().getIdLong();
        if (event.getMessage().getMentions(Message.MentionType.USER).size() > 0 && event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            userId = event.getMessage().getMentions(Message.MentionType.USER).get(0).getIdLong();
        }
        if (!DataLinkUtils.getInstance().isLinked(userId)) {
            if (userId == event.getAuthor().getIdLong()) {
                EmbedBuilder embed = MessageUtils.getBuilder(Color.RED).setDescription(event.getAuthor().getAsMention() + ", je hebt je Discord account nog niet gelinked!");
                event.getChannel().sendMessage(embed.build()).queue();
            } else {
                if (Main.getBot().getUserById(userId) != null) {
                    EmbedBuilder embed = MessageUtils.getBuilder(Color.RED)
                            .setDescription(Main.getBot().getUserById(userId).getName()
                                    + "#"
                                    + Main.getBot().getUserById(userId).getDiscriminator()
                                    + " heeft zijn Discord account nog niet gelinked!");
                    event.getChannel().sendMessage(embed.build()).queue();
                } else {
                    EmbedBuilder embed = MessageUtils.getBuilder(Color.RED)
                            .setDescription("Gebruiker niet gevonden!");
                    event.getChannel().sendMessage(embed.build()).queue();
                }
            }

            return;
        }

        OfflinePlayer p = Bukkit.getOfflinePlayer(DataLinkUtils.getInstance().getUUIDFromDiscord(userId));
        SDBPlayer sdb = SDBPlayer.createSDBPlayer(p);
        Plugin pl = Bukkit.getPluginManager().getPlugin("MinetopiaSDB");

        EmbedBuilder embed = MessageUtils.getBuilder(Color.GREEN);
        embed.addField("Speler", p.getName(), false);

        ConfigUtils cu = ConfigUtils.getInstance();

        if (cu.getShowOption(ShowOption.FITHEID)) {
            embed.addField(cu.getShowTitle(ShowOption.FITHEID), sdb.getFitheid() + "/" + pl.getConfig().getString("Fitheid.Max"), false);
        }
        if (cu.getShowOption(ShowOption.PREFIX)) {
            embed.addField(cu.getShowTitle(ShowOption.PREFIX), sdb.getPrefix(), false);
        }
        if (cu.getShowOption(ShowOption.RANK)) {
            embed.addField(cu.getShowTitle(ShowOption.RANK), sdb.getRank(), false);
        }
        if (cu.getShowOption(ShowOption.LEVEL)) {
            embed.addField(cu.getShowTitle(ShowOption.LEVEL), "" + sdb.getLevel(), false);
        }
        if (cu.getShowOption(ShowOption.MONEY)) {
            embed.addField(cu.getShowTitle(ShowOption.MONEY), "€ " + format(API.getEcon().getBalance(p)), false);
        }
        if (cu.getShowOption(ShowOption.ONLINETIME)) {
            embed.addField(cu.getShowTitle(ShowOption.ONLINETIME), "" + sdb.getTime(TimeType.DAYS) + " dagen, "
                    + sdb.getTime(TimeType.HOURS) + " uur, " + sdb.getTime(TimeType.MINUTES) + " minuten", false);
        }
        if (cu.getShowOption(ShowOption.ONLINESTATUS)) {
            embed.addField(cu.getShowTitle(ShowOption.ONLINESTATUS), p.isOnline() ? "Ja" : "Nee", false);
        }
        if (cu.getShowOption(ShowOption.GRAYCOIN)) {
            embed.addField(cu.getShowTitle(ShowOption.GRAYCOIN), "" + sdb.getGrayCoins(), false);
        }
        if (cu.getShowOption(ShowOption.LUCKYSHARD)) {
            embed.addField(cu.getShowTitle(ShowOption.LUCKYSHARD), "" + sdb.getShards(), false);
        }

        event.getChannel().sendMessage(embed.build()).queue();
    }
}
