package nl.minetopiasdb.discordbot.bukkit.commands;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.minetopiasdb.discordbot.Main;
import nl.minetopiasdb.discordbot.utils.data.ConfigUtils;
import nl.minetopiasdb.discordbot.utils.link.DataLinkUtils;
import nl.minetopiasdb.discordbot.utils.link.LinkUtils;

public class LinkCMD implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ConfigUtils.cc("&4ERROR: &cAlleen spelers kunnen hun Discord account linken."));
			return true;
		}
		if (args.length == 0) {
			sender.sendMessage(ConfigUtils.cc("&3Gebruik &b/link <Code>&3."));
			sender.sendMessage(ConfigUtils.cc("&3Deze code kan je opvragen door &b" + ConfigUtils.getInstance().getPrefix() + "link &3uit te voeren in de discord server."));
			return true;
		}
		String strUuid = args[0];
		UUID uuid = null;
		try{
			uuid = UUID.fromString(strUuid);
		}catch(Exception ex) {
			sender.sendMessage(ConfigUtils.cc("&4ERROR: &cDat is geen geldige ID!"));
			return true;
		}
		if (!LinkUtils.getInstance().isValidLink(uuid)) {
			sender.sendMessage(ConfigUtils.cc("&4ERROR: &cDat is geen bekende link-token!"));
			return true;
		}
		long userId = LinkUtils.getInstance().getUser(uuid);
		DataLinkUtils.getInstance().setLink(((Player) sender).getUniqueId(), userId);
		String name = null;
		LinkUtils.getInstance().removeLink(uuid);
		if (Main.getBot().getUserByID(userId) != null) {
			name = Main.getBot().getUserByID(userId).getName() + "#" + Main.getBot().getUserByID(userId).getDiscriminator();
			sender.sendMessage(ConfigUtils.cc("&3Succesvol jouw &bDiscord&3 account &b(&3" + name + "&b)&3 gelinked!"));
			return true;
		}
		sender.sendMessage(ConfigUtils.cc("&3Succesvol jouw &bDiscord&3 account gelinked!"));
		return true;
	}
}
