package nl.minetopiasdb.discordlinker.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.function.Consumer;

public class DirectMessageConsumer<T> implements Consumer<Throwable> {

    private User user;
    private MessageChannel channel;

    public DirectMessageConsumer(User user, MessageChannel channel) {
        this.user = user;
        this.channel = channel;
    }

    @Override
    public void accept(Throwable t) {
        if (channel != null) {
            EmbedBuilder embed = MessageUtils.getBuilder(Color.RED).setDescription(user.getAsTag() + ", je moet je privéberichten aanzetten!");
            channel.sendMessage(embed.build()).queue();
        }
    }
}
