package net.draycia.teleports.homes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import net.draycia.teleports.Teleports;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class HomeManager {

    private HashMap<UUID, ArrayList<Home>> homes = new HashMap<>();
    private Type homesType = new TypeToken<HashMap<UUID, ArrayList<Home>>>() {}.getType();

    private Teleports main;
    private Gson gson;
    private File file;

    public HomeManager(Teleports main, Gson gson, File file) {
        this.main = main;
        this.gson = gson;
        this.file = file;
    }

    public HashMap<UUID, ArrayList<Home>> getHomes() {
        return homes;
    }

    public void setup() {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            HashMap<UUID, ArrayList<Home>> loadedHomes = gson.fromJson(gson.newJsonReader(new FileReader(file)), homesType);

            if (loadedHomes != null) {
                this.homes = loadedHomes;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try (JsonWriter writer = gson.newJsonWriter(new FileWriter(file))) {
            gson.toJson(homes, homesType, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addHome(UUID uuid, Home home) {
        getPlayerHomes(uuid).add(home);
    }

    public ArrayList<Home> getPlayerHomes(UUID uuid) {
        return homes.computeIfAbsent(uuid, key -> new ArrayList<>());
    }

    public boolean canUseHome(Player player, Home home) {
        if (home.getOwner().equals(player.getUniqueId())) {
            return true;
        }

        return player.hasPermission("teleports.homes.others");
    }

    public Home getHome(UUID uuid, String name) {
        for (Home home : getPlayerHomes(uuid)) {
            if (home.getName().equalsIgnoreCase(name)) {
                return home;
            }
        }

        return null;
    }

    public boolean canMakeHome(Player player, String homeName) {
        if (getHome(player.getUniqueId(), homeName) != null) {
            // send exists message
            return false;
        }

        if (player.hasPermission("teleports.homes.set.unlimited")) {
            return true;
        }

        if (getPlayerHomeCount(player) + 1 > getPlayerhomeLimit(player)) {
            // send limit message
            return false;
        }

        return true;
    }

    public int getPlayerHomeCount(Player player) {
        return getPlayerHomes(player.getUniqueId()).size();
    }

    public int getPlayerhomeLimit(Player player) {
        int limit = 0;

        ConfigurationSection limits = main.getConfig().getConfigurationSection("limits.homes");

        for (String key : limits.getKeys(false)) {
            int entry = limits.getInt(key);

            if (entry > limit && player.hasPermission("teleports.limits.homes." + key)) {
                limit = entry;
            }
        }

        return limit;
    }
}
