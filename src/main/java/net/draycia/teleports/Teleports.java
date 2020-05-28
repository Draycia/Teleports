package net.draycia.teleports;

import co.aikar.commands.CommandIssuer;
import co.aikar.commands.PaperCommandManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.minidigger.minimessage.text.MiniMessageParser;
import net.draycia.teleports.backs.BackLocation;
import net.draycia.teleports.commands.*;
import net.draycia.teleports.listeners.TeleportListener;
import net.draycia.teleports.playerwarps.PlayerWarp;
import net.draycia.teleports.playerwarps.PlayerWarpManager;
import net.kyori.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public final class Teleports extends JavaPlugin {

    private HashMap<UUID, BackLocation> backLocations = new HashMap<>();
    private Type backType = new TypeToken<HashMap<UUID, BackLocation>>() {}.getType();
    private File backFile;

    private Economy economy;

    private PlayerWarpManager playerWarpManager;

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private YamlConfiguration language = new YamlConfiguration();

    @Override
    public void onEnable() {
        economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();

        saveDefaultConfig();

        if (!(new File(getDataFolder(), "language.yml").exists())) {
            saveResource("language.yml", false);
        }

        try {
            backFile = new File(getDataFolder(), "backs.json");

            if (!backFile.exists()) {
                backFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            language.load(new File(getDataFolder(), "language.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        setupCommands();
        setupListeners();

        try {
            HashMap<UUID, BackLocation> backs = gson.fromJson(gson.newJsonReader(new FileReader(backFile)), backType);

            if (backs != null) {
                System.out.println("not null");
                backLocations = backs;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        playerWarpManager = new PlayerWarpManager(this, gson, new File(getDataFolder(), "player-warps.json"));
        playerWarpManager.setup();
    }

    @Override
    public void onDisable() {
        playerWarpManager.save();

        try {
            gson.toJson(backLocations, backType, gson.newJsonWriter(new FileWriter(backFile))); // TODO: BAD. RECURSION.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<UUID, BackLocation> getBackLocations() {
        return backLocations;
    }

    public PlayerWarpManager getPlayerWarpManager() {
        return playerWarpManager;
    }

    public Component getMessage(String key, String... placeholders) {
        return MiniMessageParser.parseFormat(language.getString(key).replace("\\n", "\n"), placeholders);
    }

    private void setupCommands() {
        PaperCommandManager commandManager = new PaperCommandManager(this);

        commandManager.getCommandCompletions().registerCompletion("player-warp", (context) -> {
            ArrayList<String> completions = new ArrayList<>();

            CommandIssuer issuer = context.getIssuer();

            if (!issuer.isPlayer()) {
                return completions;
            }

            for (PlayerWarp playerWarp : playerWarpManager.getPlayerWarps()) {
                if (playerWarpManager.canUsePlayerWarp(issuer.getIssuer(), playerWarp)) {
                    completions.add(playerWarp.getName());
                }
            }

            return completions;
        });

        commandManager.getCommandCompletions().registerCompletion("player-warp-owned", (context) -> {
            ArrayList<String> completions = new ArrayList<>();

            for (PlayerWarp playerWarp : playerWarpManager.getPlayerWarps()) {
                if (playerWarp.getOwner().equals(context.getIssuer().getUniqueId())) {
                    completions.add(playerWarp.getName());
                }
            }

            return completions;
        });


        commandManager.getCommandContexts().registerContext(PlayerWarp.class,
                (context) -> playerWarpManager.getWarp(context.popFirstArg()));

        commandManager.registerCommand(new BackCommand(this));
        commandManager.registerCommand(new TeleportPositionCommand(this));

        commandManager.registerCommand(new PlayerWarpCommand(this));
        commandManager.registerCommand(new PlayerSetWarpCommand(this));
        commandManager.registerCommand(new PlayerEditWarpCommand(this));
        commandManager.registerCommand(new PlayerWarpsCommand(this));
    }

    private void setupListeners() {
        getServer().getPluginManager().registerEvents(new TeleportListener(this), this);
    }

    public Economy getEconomy() {
        return economy;
    }
}
