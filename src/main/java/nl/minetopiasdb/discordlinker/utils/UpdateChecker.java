package nl.minetopiasdb.discordlinker.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import nl.minetopiasdb.discordlinker.Main;

public class UpdateChecker implements Listener {

	public static final String USER_AGENT = "DiscordLinker-UA";

	public String getVersion() {
		try {
			String version = "?";
			URL website = new URL("https://content.minetopiasdb.nl/discordlinker/version.txt");
			HttpURLConnection connection = (HttpURLConnection) website.openConnection();
			connection.addRequestProperty("User-Agent", USER_AGENT);
			InputStream inputStream = connection.getInputStream();
			Scanner s = new Scanner(inputStream);
			while (s.hasNext()) {
				version = s.next();
				break;
			}
			s.close();
			return version;
		} catch (Exception ex) {
			ex.printStackTrace();
			Main.getPlugin().getLogger()
					.info("Couldn't reach content.minetopiasdb.nl to check for the latest version.");
		}
		return "?";
	}

	public boolean isUpdateAvailible() {
		try {
			String version = getVersion();
			String[] ver = version.split("\\.");
			String[] sdbver = Main.getPlugin().getDescription().getVersion().split("\\.");

			if (ver.length == 1 || ver[0].equals("?")) {
				return false;
			} else {
				if (ver.length <= 1) {
					return false;
				} else {
					if (Integer.valueOf(ver[0]) > Integer.valueOf(sdbver[0])) {
						return true;
					}
					if (Integer.valueOf(ver[1]) > Integer.valueOf(sdbver[1])) {
						return true;
					}
					if (ver.length >= 3) {
						if (Integer.valueOf(ver[1]) >= Integer.valueOf(sdbver[1])) {
							if (sdbver.length <= 2) {
								return true;
							} else if (Integer.valueOf(ver[2]) > Integer.valueOf(sdbver[2])) {
								return true;
							}
						}
					}
				}
			}
		} catch (Exception ex) {
		}
		return false;
	}

	public void sendUpdateMessage(Player p) {
		String name = Main.getPlugin().getDescription().getName();
		String newver = getVersion();
		p.sendMessage("   �3-=-=-=[�b" + name + "�3]=-=-=-   ");
		p.sendMessage("�3Er is een update beschikbaar voor �b" + name + "�3!");
		p.sendMessage("�3Je maakt nu gebruik van versie �b" + Main.getPlugin().getDescription().getVersion() + "�3.");
		p.sendMessage("�3De nieuwste versie is �b" + newver);
		p.sendMessage("�3Download deze versie van �bhttps://projects.minetopiasdb.nl/discordlinker/");
		p.sendMessage("   �3-=-=-=[�b" + name + "�3]=-=-=-   ");
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();

		if (p.isOp()) {
			Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getPlugin(), new Runnable() {
				public void run() {
					if (isUpdateAvailible()) {
						sendUpdateMessage(p);
					}
				}
			}, 39l);
		}
	}
}
