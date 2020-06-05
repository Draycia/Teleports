package net.draycia.teleports.commands.misc;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import io.papermc.lib.PaperLib;
import net.draycia.teleports.Teleports;
import net.kyori.text.adapter.bukkit.TextAdapter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

@CommandAlias("back|b")
@CommandPermission("teleports.teleport.back")
public class BackCommand extends BaseCommand {

    private Teleports main;

    public BackCommand(Teleports main) {
        this.main = main;
    }

    @Default
    @Description("Returns you to your last location")
    public void baseCommand(Player player) {
        Location location = main.getBackLocations().get(player.getUniqueId()).getLocation();

        if (location == null) {
            TextAdapter.sendMessage(player, main.getMessage("no-back-location"));
            return;
        }

        PaperLib.teleportAsync(player, location);
        TextAdapter.sendMessage(player, main.getMessage("back-success",
                "x", Teleports.FORMAT.format(location.getX()),
                "y", Teleports.FORMAT.format(location.getY()),
                "z", Teleports.FORMAT.format(location.getZ()),
                "world", location.getWorld().getName(),
                "pitch", Teleports.FORMAT.format(location.getPitch()),
                "yaw", Teleports.FORMAT.format(location.getYaw())));
    }

}
