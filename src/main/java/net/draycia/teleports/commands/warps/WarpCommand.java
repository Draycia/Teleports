package net.draycia.teleports.commands.warps;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import io.papermc.lib.PaperLib;
import net.draycia.teleports.Teleports;
import net.draycia.teleports.warps.Warp;
import net.kyori.text.adapter.bukkit.TextAdapter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@CommandAlias("warp")
@CommandPermission("teleports.warps.use")
public class WarpCommand extends BaseCommand {

    private Teleports main;

    public WarpCommand(Teleports main) {
        this.main = main;
    }

    @Default
    @CommandCompletion("@warp-usable")
    public void warp(Player player, @Conditions("warp-exists:true,can-use-warp:true") Warp warp) {
        if (!main.getWarpManager().canUseWarp(player, warp)) {
            return; // TODO: send can't use warp message
        }

        if (warp.getPrice() > 0) {
            if (!main.getEconomy().has(player, warp.getPrice())) {
                TextAdapter.sendMessage(player, main.getMessage("warp-insufficient-funds", "cost", Double.toString(warp.getPrice()), "warp", warp.getName()));
                return;
            }

            main.getEconomy().withdrawPlayer(player, warp.getPrice());
        }

        Location location = warp.getLocation();

        PaperLib.teleportAsync(player, location);

        TextAdapter.sendMessage(player, main.getMessage("warps-use",
                "x", Teleports.FORMAT.format(location.getX()),
                "y", Teleports.FORMAT.format(location.getY()),
                "z", Teleports.FORMAT.format(location.getZ()),
                "world", location.getWorld().getName(),
                "pitch", Teleports.FORMAT.format(location.getPitch()),
                "yaw", Teleports.FORMAT.format(location.getYaw()),
                "warp", warp.getName()));
    }

}
