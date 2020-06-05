package net.draycia.teleports.commands.warps;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import net.draycia.teleports.SerializableLocation;
import net.draycia.teleports.Teleports;
import net.draycia.teleports.homes.Home;
import net.kyori.text.adapter.bukkit.TextAdapter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@CommandAlias("sethome")
@CommandPermission("teleports.homes.set")
public class SetHomeCommand extends BaseCommand {

    private Teleports main;

    public SetHomeCommand(Teleports main) {
        this.main = main;
    }

    @Default
    @CommandCompletion("name")
    public void baseCommand(Player player, String homeName) {
        if (!main.getHomeManager().canMakeHome(player, homeName)) {
            return; // TODO: send message
        }

        if (!homeName.matches("^[a-zA-Z0-9_\\-]*$")) {
            TextAdapter.sendMessage(player, main.getMessage("name-format-invalid"));
            return;
        }

        Home home = main.getHomeManager().getHome(player.getUniqueId(), homeName);

        Location location = player.getLocation();

        String key = "homes-set-home";

        if (home != null) {
            home.setLocation(location);
            key = "homes-set-location";
        } else {
            home = new Home(homeName, player.getUniqueId(), new SerializableLocation(player.getLocation()));
            main.getHomeManager().addHome(player.getUniqueId(), home);
        }

        TextAdapter.sendMessage(player, main.getMessage(key,
                "x", Teleports.FORMAT.format(location.getX()),
                "y", Teleports.FORMAT.format(location.getY()),
                "z", Teleports.FORMAT.format(location.getZ()),
                "world", location.getWorld().getName(),
                "pitch", Teleports.FORMAT.format(location.getPitch()),
                "yaw", Teleports.FORMAT.format(location.getYaw()),
                "home", homeName));
    }

}
