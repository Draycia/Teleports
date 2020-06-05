package net.draycia.teleports.commands.misc;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import io.papermc.lib.PaperLib;
import net.draycia.teleports.Teleports;
import net.kyori.text.adapter.bukkit.TextAdapter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

@CommandAlias("teleportposition|teleportpos|tppos")
@CommandPermission("teleports.teleport.position")
public class TeleportPositionCommand extends BaseCommand {

    private Teleports main;
    DecimalFormat format = new DecimalFormat("#.##");

    public TeleportPositionCommand(Teleports main) {
        this.main = main;
    }

    @Default
    @CommandCompletion("x, y, z, @worlds")
    @Description("Returns you to your last location")
    public void baseCommand(Player player, double x, double y, double z, @Optional World world) {
        if (world == null) {
            world = player.getWorld();
        }

        Location location = new Location(world, x, y, z);

        PaperLib.teleportAsync(player, location);
        TextAdapter.sendMessage(player, main.getMessage("teleport-success",
                "x", format.format(location.getX()),
                "y", format.format(location.getY()),
                "z", format.format(location.getZ()),
                "world", location.getWorld().getName(),
                "pitch", format.format(location.getPitch()),
                "yaw", format.format(location.getYaw())));
    }

}
