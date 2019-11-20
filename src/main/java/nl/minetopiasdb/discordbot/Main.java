package nl.minetopiasdb.discordbot;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import nl.minetopiasdb.discordbot.bukkit.commands.LinkCMD;
import nl.minetopiasdb.discordbot.bukkit.listeners.JoinListener;
import nl.minetopiasdb.discordbot.discord.commands.DiscordLinkCMD;
import nl.minetopiasdb.discordbot.discord.commands.HelpCMD;
import nl.minetopiasdb.discordbot.discord.commands.ServerinfoCMD;
import nl.minetopiasdb.discordbot.discord.commands.StatCMD;
import nl.minetopiasdb.discordbot.utils.UpdateChecker;
import nl.minetopiasdb.discordbot.utils.data.ConfigUtils;
import nl.minetopiasdb.discordbot.utils.data.UserData;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.StatusType;

public class Main extends JavaPlugin {

	private static Plugin plugin;
	private static IDiscordClient bot;

	public void onEnable() {
		plugin = this;
		if (!Bukkit.getPluginManager().isPluginEnabled("MinetopiaSDB")) {
			Bukkit.getPluginManager().registerEvents(new Listener() {
				@EventHandler
				public void onJoin(PlayerJoinEvent e) {
					if (e.getPlayer().isOp()) {
						Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {
							public void run() {
								e.getPlayer().sendMessage(ChatColor.RED
										+ "DiscordLinker >> MinetopiaSDB is vereist om DiscordLinker te laten werken!");
							}
						}, 5 * 20l);
					}
				}
			}, Main.plugin);
		}
		ConfigUtils.getInstance().addDefault(plugin);
		UserData.getInstance().setup(plugin);
		loginBot();
		getCommand("link").setExecutor(new LinkCMD());
		Bukkit.getPluginManager().registerEvents(new UpdateChecker(), this);
		Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
	}

	public void loginBot() {
		try {
			File libs = new File(getDataFolder() + File.separator + "libs");
			if (!libs.exists()) {
				libs.mkdirs();
			}
			File file = new File(getDataFolder() + File.separator + "libs" + File.separator + "Discord4J.jar");
			if (!file.exists()) {
				getLogger().info("Discord4j not found in libs folder! Downloading..");
				FileUtils.copyURLToFile(new URL("https://content.minetopiasdb.nl/discordlinker/Discord4J.jar"), file, 10000, 10000);
				if (!file.exists() || file.getTotalSpace() == 0) {
					getLogger().warning(
							"Downloading Discord4J failed, please try again. Are you sure content.minetopiasdb.nl is accessible?");
					return;
				} else {
					getLogger().info("Discord4J download complete!");
				}
			}

			URL discord = file.toURI().toURL();
			URLClassLoader ucl = (URLClassLoader) getClassLoader();
			Method add = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			add.setAccessible(true);
			add.invoke(ucl, discord);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		ClientBuilder client = new ClientBuilder();
		String token = ConfigUtils.getInstance().getBotToken();
		try {
			client.withToken(token);
			bot = client.login();
		} catch (Exception ex) {
			if (ex.getMessage().contains("401: Unauthorized")) {
				Bukkit.getPluginManager().registerEvents(new Listener() {
					@EventHandler
					public void onJoin(PlayerJoinEvent e) {
						if (e.getPlayer().isOp()) {
							Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {
								public void run() {
									e.getPlayer().sendMessage(ChatColor.RED
											+ "DiscordLinker >> Geen geldige discordtoken ingesteld. De plugin doet nu niets!");
									e.getPlayer().sendMessage(ChatColor.RED
											+ "Voor info: https://projects.minetopiasdb.nl/discordlinker");
								}
							}, 5 * 20l);
						}
					}
				}, Main.plugin);
				Main.plugin.getLogger().warning("GEEN GELDIGE DISCORDTOKEN INGESTELD!");
				return;
			}
			ex.printStackTrace();
		}
		bot.getDispatcher().registerListener(new DiscordLinkCMD());
		bot.getDispatcher().registerListener(new StatCMD());
		bot.getDispatcher().registerListener(new ServerinfoCMD());
		bot.getDispatcher().registerListener(new HelpCMD());

		while (!bot.isReady()) {
			//wait a sec.
		}
		updatePresence();
	}

	public void onDisable() {
		if (bot != null && bot.isLoggedIn()) {
			bot.logout();
		}
	}

	public static IDiscordClient getBot() {
		return bot;
	}

	public static Plugin getPlugin() {
		return plugin;
	}
	
	public static void updatePresence() {
		getBot().changePresence(StatusType.valueOf(ConfigUtils.getInstance().getStatus()),
				ActivityType.valueOf(ConfigUtils.getInstance().getPlayingType()),
				ConfigUtils.getInstance().getPlayingMessage().replaceAll("<Spelers>", "" + Bukkit.getOnlinePlayers().size()));
	}
}
