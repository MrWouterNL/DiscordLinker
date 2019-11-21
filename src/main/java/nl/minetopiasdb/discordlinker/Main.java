package nl.minetopiasdb.discordlinker;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.managers.Presence;
import nl.minetopiasdb.discordlinker.bukkit.commands.DCLinkerCMD;
import nl.minetopiasdb.discordlinker.bukkit.commands.LinkCMD;
import nl.minetopiasdb.discordlinker.bukkit.listeners.JoinListener;
import nl.minetopiasdb.discordlinker.discord.commands.*;
import nl.minetopiasdb.discordlinker.discord.listeners.CommandListener;
import nl.minetopiasdb.discordlinker.utils.Updat3r;
import nl.minetopiasdb.discordlinker.utils.commands.CommandFactory;
import nl.minetopiasdb.discordlinker.utils.data.ConfigUtils;
import nl.minetopiasdb.discordlinker.utils.data.UserData;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class Main extends JavaPlugin {

    private static Plugin plugin;
    private static JDA bot;

    public static JDA getBot() {
        return bot;
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static void updatePresence() {
        Presence presence = getBot().getPresence();

        presence.setStatus(OnlineStatus.valueOf(ConfigUtils.getInstance().getStatus()));
        switch (ConfigUtils.getInstance().getPlayingType().toLowerCase()) {
            case "playing":
                presence.setActivity(Activity.playing(ConfigUtils.getInstance().getPlayingMessage().replaceAll("<Spelers>", "" + Bukkit.getOnlinePlayers().size())));
                break;
            case "watching":
                presence.setActivity(Activity.watching(ConfigUtils.getInstance().getPlayingMessage().replaceAll("<Spelers>", "" + Bukkit.getOnlinePlayers().size())));
                break;
            case "listening":
                presence.setActivity(Activity.listening(ConfigUtils.getInstance().getPlayingMessage().replaceAll("<Spelers>", "" + Bukkit.getOnlinePlayers().size())));
                break;
            case "streaming":
                presence.setActivity(Activity.streaming(ConfigUtils.getInstance().getPlayingMessage().replaceAll("<Spelers>", "" + Bukkit.getOnlinePlayers().size()), null));
                break;
        }
    }

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
        getCommand("dclinker").setExecutor(new DCLinkerCMD());
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Updat3r.getInstance().startTask();
    }

    public void loginBot() {
        try {
            File libs = new File(getDataFolder() + File.separator + "libs");
            if (!libs.exists()) {
                libs.mkdirs();
            }
            File jdafile = new File(getDataFolder() + File.separator + "libs" + File.separator + "JDA-4.0.0_62.jar");
            if (!jdafile.exists()) {
                File[] files = libs.listFiles();
                for (File file : files) {
                    if (file.getName().startsWith("JDA")) {
                        file.delete();
                    }
                }
                getLogger().info("JDA not found in libs folder! Downloading..");
                FileUtils.copyURLToFile(new URL("https://ci.dv8tion.net/job/JDA/62/artifact/build/libs/JDA-4.0.0_62-withDependencies-no-opus.jar"), jdafile, 10000, 10000);
                if (!jdafile.exists() || jdafile.getTotalSpace() == 0) {
                    getLogger().warning(
                            "Downloading JDA failed, please try again. Are you sure ci.dv8tion.net is accessible?");
                    return;
                } else {
                    getLogger().info("JDA download complete!");
                }
            }

            URL discord = jdafile.toURI().toURL();
            URLClassLoader ucl = (URLClassLoader) getClassLoader();
            Method add = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            add.setAccessible(true);
            add.invoke(ucl, discord);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String token = ConfigUtils.getInstance().getBotToken();
        JDABuilder builder = new JDABuilder(AccountType.BOT)
                .setToken(token)
                .setActivity(Activity.playing("loading"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB);

        builder.addEventListeners(new CommandListener());

        CommandFactory.getInstance().registerCommand("link", new DiscordLinkCMD());
        CommandFactory.getInstance().registerCommand("unlink", new DiscordUnlinkCMD());
        CommandFactory.getInstance().registerCommand("stats", new StatCMD());
        CommandFactory.getInstance().registerCommand("server", new ServerinfoCMD());
        CommandFactory.getInstance().registerCommand("help", new HelpCMD());

        try {
            bot = builder.build();
        } catch (Exception ex) {
            if (ex.getMessage().contains("The provided token is invalid")) {
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

        updatePresence();
    }

    public void onDisable() {
        if (bot != null && bot.getStatus().isInit()) {
            bot.shutdownNow();
        }
    }
}
