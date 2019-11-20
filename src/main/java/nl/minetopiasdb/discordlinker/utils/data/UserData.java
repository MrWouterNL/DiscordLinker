package nl.minetopiasdb.discordlinker.utils.data;

import nl.minetopiasdb.discordlinker.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;


public class UserData {

    private static UserData instance = new UserData();
    FileConfiguration PlayerData;
    File pdfile;

    public static UserData getInstance() {
        return instance;
    }

    public void setup(Plugin p) {
        pdfile = new File(p.getDataFolder(), "Data/Spelers.yml");
        PlayerData = YamlConfiguration.loadConfiguration(pdfile);
    }

    public FileConfiguration getData() {
        return PlayerData;
    }

    public void reloadData() {
        PlayerData = YamlConfiguration.loadConfiguration(pdfile);
    }

    public void saveData() {
        try {
            if (PlayerData != null && pdfile != null) {
                PlayerData.save(pdfile);
            } else {
                setup(Main.getPlugin());
                PlayerData.save(pdfile);
            }
        } catch (Exception e) {
            Main.getPlugin().getLogger().info(ChatColor.RED + "Het opslaan van Spelers.yml is niet gelukt!");
            e.printStackTrace();
        }
    }
}