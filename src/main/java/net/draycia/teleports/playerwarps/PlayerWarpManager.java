package net.draycia.teleports.playerwarps;

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
import java.util.UUID;

public class PlayerWarpManager {

    private Teleports main;

    private ArrayList<PlayerWarp> playerWarps = new ArrayList<>();
    private Type playerWarpsType = new TypeToken<ArrayList<PlayerWarp>>() {}.getType();

    private Gson gson;
    private File file;

    public PlayerWarpManager(Teleports main, Gson gson, File file) {
        this.main = main;
        this.gson = gson;
        this.file = file;
    }

    public ArrayList<PlayerWarp> getPlayerWarps() {
        return playerWarps;
    }

    public void setup() {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            ArrayList<PlayerWarp> warps = gson.fromJson(gson.newJsonReader(new FileReader(file)), playerWarpsType);

            if (warps != null) {
                this.playerWarps = warps;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try (JsonWriter writer = gson.newJsonWriter(new FileWriter(file))) {
            gson.toJson(playerWarps, playerWarpsType, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPlayerWarp(PlayerWarp playerWarp) {
        this.playerWarps.add(playerWarp);
    }

    public boolean canUsePlayerWarp(Player issuer, PlayerWarp playerWarp) {
        UUID uuid = issuer.getUniqueId();

        return playerWarp.isPublic() || playerWarp.getOwner().equals(uuid) || playerWarp.getMembers().contains(uuid) ||
                issuer.hasPermission("teleports.pwarps.use." + playerWarp.getWarpId().toString());
    }

    public PlayerWarp getWarp(String name) {
        for (PlayerWarp playerWarp : playerWarps) {
            if (playerWarp.getName().equalsIgnoreCase(name)) {
                return playerWarp;
            }
        }

        return null;
    }

    public PlayerWarp getWarp(UUID uuid) {
        for (PlayerWarp playerWarp : playerWarps) {
            if (playerWarp.getWarpId().equals(uuid)) {
                return playerWarp;
            }
        }

        return null;
    }

    public boolean canMakePlayerWarp(Player player, String warpName) {
        if (getWarp(warpName) != null) {
            // send exists message
            return false;
        }

        if (player.hasPermission("teleports.pwarp.set.unlimited")) {
            return true;
        }

        if (getPlayerWarpCount(player) + 1 > getPlayerWarpLimit(player)) {
            // send limit message
            return false;
        }

        return true;
    }

    public int getPlayerWarpCount(Player player) {
        int count = 0;

        for (PlayerWarp warp : playerWarps) {
            if (warp.getOwner().equals(player.getUniqueId())) {
                count++;
            }
        }

        return count;
    }

    public int getPlayerWarpLimit(Player player) {
        int limit = 0;

        ConfigurationSection limits = main.getConfig().getConfigurationSection("limits");

        for (String key : limits.getKeys(false)) {
            int entry = limits.getInt(key);

            if (entry > limit && player.hasPermission("teleports.limits." + key)) {
                limit = entry;
            }
        }

        return limit;
    }

}
