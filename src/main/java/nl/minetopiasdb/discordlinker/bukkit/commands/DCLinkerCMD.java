package nl.minetopiasdb.discordlinker.bukkit.commands;

import nl.minetopiasdb.discordlinker.Main;
import nl.minetopiasdb.discordlinker.utils.Updat3r;
import nl.minetopiasdb.discordlinker.utils.data.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DCLinkerCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage("   §3-=-=-=[§bDiscordLinker§3]=-=-=-   ");
            sender.sendMessage("§3Je maakt nu gebruik van DiscordLinker versie §b" + Main.getPlugin().getDescription().getVersion() + "§3.");
            sender.sendMessage("   §3-=-=-=[§bDiscordLinker§3]=-=-=-   ");
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("updateinfo")) {
                sender.sendMessage(
                        ConfigUtils.cc("&3Cached 'latest': &b" + Updat3r.getInstance().getLatestCached().getVersion()));
                sender.sendMessage(ConfigUtils.cc("&3Latest: &b"
                        + Updat3r.getInstance().getLatestUpdate(Updat3r.PROJECT_NAME, Updat3r.API_KEY).getVersion()));
                sender.sendMessage(ConfigUtils.cc("&3Actual version: &b" + Main.getPlugin().getDescription().getVersion()));
            }
            if (args[0].equalsIgnoreCase("update")) {
                if (!Updat3r.getInstance().getLatestCached().isNewer()) {
                    sender.sendMessage(ConfigUtils.cc("&cEr is geen update beschikbaar!"));
                    return true;
                }
                sender.sendMessage(ConfigUtils.cc("&3We gaan de update nu installeren!"));
                Updat3r.getInstance().downloadLatest(Updat3r.getInstance().getLatestCached().getDownloadLink(),
                        "DiscordLinker", Main.getPlugin());
                Bukkit.reload();
            }
        }
        return true;
    }
}