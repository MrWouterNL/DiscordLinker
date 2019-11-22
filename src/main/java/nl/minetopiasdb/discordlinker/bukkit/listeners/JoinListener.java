package nl.minetopiasdb.discordlinker.bukkit.listeners;

import nl.minetopiasdb.discordlinker.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        updateAsync();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        updateAsync();
    }

    public void updateAsync() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
            public void run() {
                Main.updatePresence();
            }
        });
    }
}
