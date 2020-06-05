package net.draycia.teleports.commands.pwarps;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import net.draycia.teleports.Teleports;
import net.draycia.teleports.playerwarps.PlayerWarp;
import net.kyori.text.Component;
import net.kyori.text.adapter.bukkit.TextAdapter;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@CommandAlias("setplayerwarp|setpwarp|psetwarp|psw")
@CommandPermission("teleports.pwarps.set")
public class PlayerSetWarpCommand extends BaseCommand {

    private Teleports main;

    public PlayerSetWarpCommand(Teleports main) {
        this.main = main;
    }

    @Default
    public void baseCommand(Player player, String warpName) {
        PlayerWarp oldWarp = main.getPlayerWarpManager().getWarp(warpName);

        Location location = player.getLocation();

        if (oldWarp != null && oldWarp.getOwner().equals(player.getUniqueId())) {
            Component log = main.getMessage("pwarp-location-changed",
                    "x", Teleports.FORMAT.format(location.getX()),
                    "y", Teleports.FORMAT.format(location.getY()),
                    "z", Teleports.FORMAT.format(location.getZ()),
                    "world", location.getWorld().getName(),
                    "pitch", Teleports.FORMAT.format(location.getPitch()),
                    "yaw", Teleports.FORMAT.format(location.getYaw()),
                    "pwarp", oldWarp.getName(),
                    "oldx", Teleports.FORMAT.format(oldWarp.getLocation().getX()),
                    "oldy", Teleports.FORMAT.format(oldWarp.getLocation().getY()),
                    "oldz", Teleports.FORMAT.format(oldWarp.getLocation().getZ()),
                    "oldworld", oldWarp.getLocation().getWorld().getName(),
                    "oldpitch", Teleports.FORMAT.format(oldWarp.getLocation().getPitch()),
                    "oldyaw", Teleports.FORMAT.format(oldWarp.getLocation().getYaw()));

            main.getLogger().info(LegacyComponentSerializer.legacy().serialize(log));

            oldWarp.setLocation(player.getLocation());

            TextAdapter.sendMessage(player, main.getMessage("pwarp-set-location",
                    "x", Teleports.FORMAT.format(location.getX()),
                    "y", Teleports.FORMAT.format(location.getY()),
                    "z", Teleports.FORMAT.format(location.getZ()),
                    "world", location.getWorld().getName(),
                    "pitch", Teleports.FORMAT.format(location.getPitch()),
                    "yaw", Teleports.FORMAT.format(location.getYaw()),
                    "pwarp", oldWarp.getName()));

            return;
        }

        if (!main.getPlayerWarpManager().canMakePlayerWarp(player, warpName)) {
            return; // TODO: send message
        }

        if (!warpName.matches("^[a-zA-Z0-9_\\-]*$")) {
            TextAdapter.sendMessage(player, main.getMessage("name-format-invalid"));
            return;
        }

        PlayerWarp warp = new PlayerWarp(location, warpName, false,
                0, player.getUniqueId(), new ArrayList<>());

        main.getPlayerWarpManager().addPlayerWarp(warp);

        TextAdapter.sendMessage(player, main.getMessage("pwarp-create-success",
                "x", Teleports.FORMAT.format(location.getX()),
                "y", Teleports.FORMAT.format(location.getY()),
                "z", Teleports.FORMAT.format(location.getZ()),
                "world", location.getWorld().getName(),
                "pitch", Teleports.FORMAT.format(location.getPitch()),
                "yaw", Teleports.FORMAT.format(location.getYaw()),
                "pwarp", warpName));

        TextAdapter.sendMessage(player, main.getMessage("pwarp-info"));
    }

}
