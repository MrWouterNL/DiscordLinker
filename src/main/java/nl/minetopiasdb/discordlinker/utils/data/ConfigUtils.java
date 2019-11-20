package nl.minetopiasdb.discordlinker.utils.data;

import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;
import nl.minetopiasdb.discordlinker.Main;

public class ConfigUtils {

	private static ConfigUtils conf = new ConfigUtils();
	private Plugin plug;
	private static String uuidRegex = "/^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$/";

	public static ConfigUtils getInstance() {
		return conf;
	}

	public void addDefault(Plugin plug) {
		this.plug = plug;
		
		plug.getConfig().addDefault("ServerInfo.IP", "Pas mij aan in jouw config.yml!");
		
		plug.getConfig().addDefault("Discord.CommandPrefix", "mt!");
		plug.getConfig().addDefault("Discord.BotToken", "VUL HIER JE BOTTOKEN IN!");
		
		// Status: DND, IDLE, ONLINE, INVISIBLE
		// ActivityType: PLAYING, LISTENING, WATCHING, STREAMING
		
		plug.getConfig().set("Discord.Playing.StatusUitleg", "Mogelijke opties: ONLINE, DND, IDLE, INVISIBLE");
		plug.getConfig().addDefault("Discord.Playing.Status", "ONLINE");
		plug.getConfig().set("Discord.Playing.TypeUitleg", "Mogelijke opties: PLAYING, LISTENINg, WATCHING, STREAMING");
		plug.getConfig().addDefault("Discord.Playing.Type", "WATCHING");
		plug.getConfig().addDefault("Discord.Playing.Message", "Naar <Spelers> spelers ");
		
		plug.getConfig().addDefault("Discord.Message.Header", "MINETOPIASDB.NL INFO");
		plug.getConfig().addDefault("Discord.Message.Footer", "Powered by MinetopiaSDB.nl");

		for (ShowOption so : ShowOption.values()) {
			if (so == ShowOption.RANK || so == ShowOption.GRAYCOIN) {
				plug.getConfig().addDefault("Discord.StatCMD.Show" + so.toString(), false);
			} else {
				plug.getConfig().addDefault("Discord.StatCMD.Show" + so.toString(), true);
			}
		}
		
		plug.getConfig().addDefault("Discord.StatCMD.Title" + ShowOption.FITHEID.toString(), "Fitheid");
		plug.getConfig().addDefault("Discord.StatCMD.Title" + ShowOption.MONEY.toString(), "Saldo");
		plug.getConfig().addDefault("Discord.StatCMD.Title" + ShowOption.PREFIX.toString(), "Baan");
		plug.getConfig().addDefault("Discord.StatCMD.Title" + ShowOption.ONLINETIME.toString(), "Speeltijd");
		plug.getConfig().addDefault("Discord.StatCMD.Title" + ShowOption.LEVEL.toString(), "Level");
		plug.getConfig().addDefault("Discord.StatCMD.Title" + ShowOption.RANK.toString(), "Rank");
		plug.getConfig().addDefault("Discord.StatCMD.Title" + ShowOption.ONLINESTATUS.toString(), "Op dit moment online?");
		plug.getConfig().addDefault("Discord.StatCMD.Title" + ShowOption.GRAYCOIN.toString(), "GrayCoins");
		
		
		plug.getConfig().options().copyDefaults(true);
		plug.saveConfig();
	}

	public String getStatus() {
		return plug.getConfig().getString("Discord.Playing.Status");
	}
	
	public String getPlayingType() {
		return plug.getConfig().getString("Discord.Playing.Type");
	}
	
	public String getPlayingMessage() {
		return plug.getConfig().getString("Discord.Playing.Message");
	}
	
	public String getIP() {
		return plug.getConfig().getString("ServerInfo.IP");
	}
	
	public String getPrefix() {
		return plug.getConfig().getString("Discord.CommandPrefix");
	}

	public String getBotToken() {
		return plug.getConfig().getString("Discord.BotToken");
	}

	public String getHeader() {
		return plug.getConfig().getString("Discord.Message.Header");
	}

	public String getFooter() {
		return plug.getConfig().getString("Discord.Message.Footer");
	}
	
	public String getLogo() {
		return Main.getBot().getApplicationIconURL() == null ? "https://content.minetopiasdb.nl/SDBLogo.png" : Main.getBot().getApplicationIconURL();
	}

	public boolean getShowOption(ShowOption opt) {
		return plug.getConfig().getBoolean("Discord.StatCMD.Show" + opt.toString());
	}
	
	public String getShowTitle(ShowOption opt) {
		return plug.getConfig().getString("Discord.StatCMD.Title" + opt.toString());
	}
	
	public static String cc(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	public static boolean isValidUUID(String uuid) {
		return uuid.matches(uuidRegex);
	}
	
	
	public enum ShowOption {
		FITHEID, MONEY, PREFIX, ONLINETIME, LEVEL, RANK, ONLINESTATUS, GRAYCOIN;
	}
}
