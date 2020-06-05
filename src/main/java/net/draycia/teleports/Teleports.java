package net.draycia.teleports;

import co.aikar.commands.CommandIssuer;
import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.minidigger.minimessage.text.MiniMessageParser;
import net.draycia.teleports.commands.homes.HomeCommand;
import net.draycia.teleports.commands.misc.BackCommand;
import net.draycia.teleports.commands.misc.TeleportPositionCommand;
import net.draycia.teleports.commands.misc.TeleportReloadCommand;
import net.draycia.teleports.commands.pwarps.PlayerEditWarpCommand;
import net.draycia.teleports.commands.pwarps.PlayerSetWarpCommand;
import net.draycia.teleports.commands.pwarps.PlayerWarpCommand;
import net.draycia.teleports.commands.pwarps.PlayerWarpsCommand;
import net.draycia.teleports.commands.warps.SetHomeCommand;
import net.draycia.teleports.commands.warps.SetWarpCommand;
import net.draycia.teleports.commands.warps.WarpCommand;
import net.draycia.teleports.homes.Home;
import net.draycia.teleports.homes.HomeManager;
import net.draycia.teleports.listeners.TeleportListener;
import net.draycia.teleports.playerwarps.PlayerWarp;
import net.draycia.teleports.playerwarps.PlayerWarpManager;
import net.draycia.teleports.warps.Warp;
import net.draycia.teleports.warps.WarpManager;
import net.kyori.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public final class Teleports extends JavaPlugin {

    private HashMap<UUID, SerializableLocation> backLocations = new HashMap<>();
    private Type backType = new TypeToken<HashMap<UUID, SerializableLocation>>() {}.getType();
    private File backFile;

    private Economy economy;

    private PlayerWarpManager playerWarpManager;
    private WarpManager warpManager;
    private HomeManager homeManager;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private YamlConfiguration language = new YamlConfiguration();

    public static DecimalFormat FORMAT = new DecimalFormat("#.##");

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

        reloadLanguage();

        setupCommands();
        setupListeners();

        try {
            HashMap<UUID, SerializableLocation> backs = gson.fromJson(gson.newJsonReader(new FileReader(backFile)), backType);

            if (backs != null) {
                backLocations = backs;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        playerWarpManager = new PlayerWarpManager(this, gson, new File(getDataFolder(), "player-warps.json"));
        playerWarpManager.setup();

        warpManager = new WarpManager(this, gson, new File(getDataFolder(), "warps.json"));
        warpManager.setup();

        homeManager = new HomeManager(this, gson, new File(getDataFolder(), "homes.json"));
        homeManager.setup();
    }

    @Override
    public void onDisable() {
        playerWarpManager.save();
        warpManager.save();
        homeManager.save();

        try {
            gson.toJson(backLocations, backType, gson.newJsonWriter(new FileWriter(backFile)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<UUID, SerializableLocation> getBackLocations() {
        return backLocations;
    }

    public Component getMessage(String key, String... placeholders) {
        return MiniMessageParser.parseFormat(language.getString(key).replace("\\n", "\n"), placeholders);
    }

    public void reloadLanguage() {
        try {
            language.load(new File(getDataFolder(), "language.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void setupCommands() {
        PaperCommandManager commandManager = new PaperCommandManager(this);

        commandManager.getCommandCompletions().registerCompletion("boolean", (context) -> {
            return ImmutableList.of("true", "false");
        });

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

        commandManager.getCommandCompletions().registerCompletion("player-warp-members", (context) -> {
            ArrayList<String> completions = new ArrayList<>();

            String[] input = context.getInput().split(" ");

            if (input.length < 2) {
                return completions;
            }

            PlayerWarp playerWarp = getPlayerWarpManager().getWarp(input[1]);

            if (playerWarp == null || !playerWarp.getOwner().equals(context.getPlayer().getUniqueId())) {
                return completions;
            }

            for (UUID uuid : playerWarp.getMembers()) {
                completions.add(Bukkit.getOfflinePlayer(uuid).getName());
            }

            return completions;
        });

        commandManager.getCommandCompletions().registerCompletion("warp-usable", (context) -> {
            ArrayList<String> completions = new ArrayList<>();

            for (Warp warp : warpManager.getWarps()) {
                if (context.getIssuer().hasPermission("teleports.warp.warps." + warp.getName())) {
                    completions.add(warp.getName());
                }
            }

            return completions;
        });

        commandManager.getCommandCompletions().registerCompletion("homes", (context) -> {
            ArrayList<String> completions = new ArrayList<>();

            String input = context.getInput();

            if (input.contains(":") && context.getIssuer().hasPermission("teleports.homes.others")) {
                String[] pieces = input.split(":");

                for (Home home : homeManager.getPlayerHomes(Bukkit.getOfflinePlayer(pieces[0]).getUniqueId())) {
                    completions.add(pieces[0] + ":" + home.getName());
                }

                if (!completions.isEmpty()) {
                    System.out.println("true");
                    return completions;
                }
            } else {
                for (Home home : homeManager.getPlayerHomes(context.getPlayer().getUniqueId())) {
                    completions.add(home.getName());
                }
            }

            if (completions.isEmpty() && context.getIssuer().hasPermission("teleports.homes.others")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    completions.add(player.getName());
                }
            }

            return completions;
        });

        commandManager.getCommandConditions().addCondition(PlayerWarp.class, "pwarp-owner", (context, execution, value) -> {
            if (value == null || !value.getOwner().equals(context.getIssuer().getUniqueId())) {
                throw  new ConditionFailedException("You do not own that player warp.");
            }
        });

        commandManager.getCommandConditions().addCondition(PlayerWarp.class, "pwarp-exists", (context, execution, value) -> {
            if (value == null) {
                throw new ConditionFailedException("Player Warp does not exist.");
            }
        });

        commandManager.getCommandConditions().addCondition(Warp.class, "warp-can-use", (context, execution, value) -> {
            if (!context.getIssuer().hasPermission("teleports.warp.warps." + value.getName())) {
                throw new ConditionFailedException("You can't use that warp!");
            }
        });

        commandManager.getCommandConditions().addCondition(Warp.class, "warp-exists", (context, execution, value) -> {
            if (value == null) {
                throw new ConditionFailedException("Warp does not exist.");
            }
        });

        commandManager.getCommandConditions().addCondition(Home.class, "can-use-home", (context, execution, value) -> {
            if (value == null) {
                return;
            }

            CommandIssuer issuer = context.getIssuer();

            if (!issuer.getUniqueId().equals(value.getOwner()) && !issuer.hasPermission("teleports.homes.others")) {
                throw new ConditionFailedException("You cannot use other people's homes!");
            }
        });

        commandManager.getCommandContexts().registerContext(PlayerWarp.class, (context) -> {
            return playerWarpManager.getWarp(context.popFirstArg());
        });

        commandManager.getCommandContexts().registerContext(Warp.class, (context) -> {
            return warpManager.getWarp(context.popFirstArg());
        });

        commandManager.getCommandContexts().registerContext(Home.class, (context) -> {
            String arg = context.popFirstArg();

            if (arg.contains(":")) {
                String[] pieces = arg.split(":");

                return homeManager.getHome(Bukkit.getOfflinePlayer(pieces[0]).getUniqueId(), pieces[1]);
            } else {
                return homeManager.getHome(context.getPlayer().getUniqueId(), arg);
            }
        });

        // Misc
        commandManager.registerCommand(new BackCommand(this));
        commandManager.registerCommand(new TeleportPositionCommand(this));
        commandManager.registerCommand(new TeleportReloadCommand(this));

        // Player Warps
        commandManager.registerCommand(new PlayerWarpCommand(this));
        commandManager.registerCommand(new PlayerSetWarpCommand(this));
        commandManager.registerCommand(new PlayerEditWarpCommand(this));
        commandManager.registerCommand(new PlayerWarpsCommand(this));

        // Admin Warps
        commandManager.registerCommand(new SetWarpCommand(this));
        commandManager.registerCommand(new WarpCommand(this));

        // Player Homes
        commandManager.registerCommand(new SetHomeCommand(this));
        commandManager.registerCommand(new HomeCommand(this));
    }

    private void setupListeners() {
        getServer().getPluginManager().registerEvents(new TeleportListener(this), this);
    }

    public Economy getEconomy() {
        return economy;
    }

    public WarpManager getWarpManager() {
        return warpManager;
    }

    public HomeManager getHomeManager() {
        return homeManager;
    }

    public PlayerWarpManager getPlayerWarpManager() {
        return playerWarpManager;
    }

}
