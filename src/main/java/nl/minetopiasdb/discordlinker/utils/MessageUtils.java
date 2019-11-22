package nl.minetopiasdb.discordlinker.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import nl.minetopiasdb.discordlinker.utils.data.ConfigUtils;

import java.awt.*;

public class MessageUtils {

    public static EmbedBuilder getBuilder(Color c) {
        return new EmbedBuilder()
                .setAuthor(ConfigUtils.getInstance().getHeader(), null, ConfigUtils.getInstance().getLogo())
                .setFooter(ConfigUtils.getInstance().getFooter(), ConfigUtils.getInstance().getLogo())
                .setColor(c);
    }

    public static void sendPrivateAndCheckIfCanReceive(MessageChannel channel, User user, EmbedBuilder builder) {
        user.openPrivateChannel().queue((privateChannel) -> {
            privateChannel.sendMessage(builder.build()).queue();
        }, new DirectMessageConsumer<Throwable>(user, channel));
    }
}
