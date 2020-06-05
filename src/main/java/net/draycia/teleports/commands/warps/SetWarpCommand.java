package net.draycia.teleports.commands.warps;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import net.draycia.teleports.Teleports;
import net.draycia.teleports.warps.Warp;
import net.kyori.text.adapter.bukkit.TextAdapter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@CommandAlias("setwarp")
@CommandPermission("teleports.warps.set")
public class SetWarpCommand extends BaseCommand {

    private Teleports main;

    public SetWarpCommand(Teleports main) {
        this.main = main;
    }

    @Default
    @CommandCompletion("name")
    public void baseCommand(Player player, String warpName) {
        if (!warpName.matches("^[a-zA-Z0-9_\\-]*$")) {
            TextAdapter.sendMessage(player, main.getMessage("name-format-invalid"));
            return;
        }

        Warp warp = main.getWarpManager().getWarp(warpName);

        Location location = player.getLocation();

        String key = "warps-set-warp";


        if (warp != null) {
            warp.setLocation(location);
            key = "warps-set-location";
        } else {
            warp = new Warp(player.getLocation(), player.getUniqueId(), 0, false, warpName);
            main.getWarpManager().addWarp(warp);
        }

        TextAdapter.sendMessage(player, main.getMessage(key,
                "x", Teleports.FORMAT.format(location.getX()),
                "y", Teleports.FORMAT.format(location.getY()),
                "z", Teleports.FORMAT.format(location.getZ()),
                "world", location.getWorld().getName(),
                "pitch", Teleports.FORMAT.format(location.getPitch()),
                "yaw", Teleports.FORMAT.format(location.getYaw()),
                "warp", warpName));
    }

}
