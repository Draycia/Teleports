package net.draycia.teleports.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import io.papermc.lib.PaperLib;
import net.draycia.teleports.playerwarps.PlayerWarp;
import net.draycia.teleports.Teleports;
import net.kyori.text.adapter.bukkit.TextAdapter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

@CommandAlias("playerwarp|pwarp|pw")
@CommandPermission("teleports.pwarps.warp")
public class PlayerWarpCommand extends BaseCommand {

    private Teleports main;
    DecimalFormat format = new DecimalFormat("#.##");

    public PlayerWarpCommand(Teleports main) {
        this.main = main;
    }

    @Default
    @CommandCompletion("@player-warp")
    public void baseCommand(Player player, @Conditions("pwarp-exists") PlayerWarp playerWarp) {
        if (!main.getPlayerWarpManager().canUsePlayerWarp(player, playerWarp)) {
            return;
        }

        if (playerWarp.getPrice() > 0) {
            if (!main.getEconomy().has(player, playerWarp.getPrice())) {
                TextAdapter.sendMessage(player, main.getMessage("pwarp-insufficient-funds", "cost", Double.toString(playerWarp.getPrice()), "pwarp", playerWarp.getName()));
                return;
            }

            main.getEconomy().withdrawPlayer(player, playerWarp.getPrice());
            main.getEconomy().depositPlayer(player, playerWarp.getPrice());
        }

        Location location = playerWarp.getLocation().getLocation();

        PaperLib.teleportAsync(player, location);

        TextAdapter.sendMessage(player, main.getMessage("pwarp-success",
                "x", format.format(location.getX()),
                "y", format.format(location.getY()),
                "z", format.format(location.getZ()),
                "world", location.getWorld().getName(),
                "pitch", format.format(location.getPitch()),
                "yaw", format.format(location.getYaw()),
                "pwarp", playerWarp.getName()));

        TextAdapter.sendMessage(player, main.getMessage("pwarp-info"));
    }

}
