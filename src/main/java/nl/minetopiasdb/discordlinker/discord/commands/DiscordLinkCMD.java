package nl.minetopiasdb.discordlinker.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import nl.minetopiasdb.discordlinker.utils.MessageUtils;
import nl.minetopiasdb.discordlinker.utils.commands.BotCommand;
import nl.minetopiasdb.discordlinker.utils.commands.Command;
import nl.minetopiasdb.discordlinker.utils.link.DataLinkUtils;
import nl.minetopiasdb.discordlinker.utils.link.LinkUtils;

import java.awt.*;
import java.util.UUID;

public class DiscordLinkCMD implements BotCommand {

    @Override
    public void excecute(Command cmd, String[] args, Message msg, MessageReceivedEvent event) {
        long id = event.getAuthor().getIdLong();
        if (DataLinkUtils.getInstance().isLinked(id)) {
            EmbedBuilder embed = MessageUtils.getBuilder(Color.RED)
                    .setDescription("Je hebt je Discord account al gelinked!");
            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }
        if (LinkUtils.getInstance().isValidLink(id)) {
            LinkUtils.getInstance().removeLink(id);
        }
        UUID linkUUID = LinkUtils.getInstance().registerLink(id);

        event.getAuthor().openPrivateChannel().queue((privateChannel) -> {
            EmbedBuilder embed = MessageUtils.getBuilder(Color.GREEN)
                    .setDescription("Type het volgende commando in Minecraft om jouw account te linken: ```/link " + linkUUID.toString() + "```");
            privateChannel.sendMessage(embed.build()).queue();
        }, (error) -> {
            EmbedBuilder embed = MessageUtils.getBuilder(Color.RED).setDescription(event.getAuthor().getAsTag() + ", je moet je privéberichten aanzetten!");
            event.getChannel().sendMessage(embed.build()).queue();
            LinkUtils.getInstance().removeLink(linkUUID);
        });
    }
}
