package net.draycia.teleports.warps;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import net.draycia.teleports.Teleports;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class WarpManager {

    private Teleports main;

    private ArrayList<Warp> warps = new ArrayList<>();
    private Type warpsType = new TypeToken<ArrayList<Warp>>() {}.getType();

    private Gson gson;
    private File file;

    public WarpManager(Teleports main, Gson gson, File file) {
        this.main = main;
        this.gson = gson;
        this.file = file;
    }

    public ArrayList<Warp> getWarps() {
        return warps;
    }

    public void setup() {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            ArrayList<Warp> loadedWarps = gson.fromJson(gson.newJsonReader(new FileReader(file)), warpsType);

            if (loadedWarps != null) {
                this.warps = loadedWarps;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try (JsonWriter writer = gson.newJsonWriter(new FileWriter(file))) {
            gson.toJson(warps, warpsType, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addWarp(Warp warp) {
        this.warps.add(warp);
    }

    public boolean canUseWarp(Player issuer, Warp warp) {
        return issuer.hasPermission("teleports.warps.use." + warp.getName());
    }

    public Warp getWarp(String name) {
        for (Warp warp : warps) {
            if (warp.getName().equalsIgnoreCase(name)) {
                return warp;
            }
        }

        return null;
    }

}
