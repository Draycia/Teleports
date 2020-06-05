package net.draycia.teleports.commands.homes;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import io.papermc.lib.PaperLib;
import net.draycia.teleports.Teleports;
import net.draycia.teleports.homes.Home;
import net.kyori.text.Component;
import net.kyori.text.adapter.bukkit.TextAdapter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;

@CommandAlias("home|homes")
@CommandPermission("teleports.homes.use")
public class HomeCommand extends BaseCommand {

    private Teleports main;

    public HomeCommand(Teleports main) {
        this.main = main;
    }

    @Default
    @CommandCompletion("@homes")
    public void baseCommand(Player player, @Optional @Conditions("can-use-home:true") Home home) {
        // TODO: delhome command
        // TODO: homeinfo command
        // No args provided
        if (home == null) {
            ArrayList<Home> homes = main.getHomeManager().getPlayerHomes(player.getUniqueId());

            if (homes.size() == 0) {
                // NO HOMES
                return;
            }

            Component component = main.getMessage("homes-list", "amount", Integer.toString(homes.size()));

            Iterator<Home> iterator = homes.iterator();
            Home entry;

            while (iterator.hasNext()) {
                entry = iterator.next();

                component.append(main.getMessage("homes-entry", "home", entry.getName()));
            }

            // TODO: fix home list not showing homes??
            TextAdapter.sendMessage(player, component);

            return;
        }

        PaperLib.teleportAsync(player, home.getLocation());
        TextAdapter.sendMessage(player, main.getMessage("homes-success", "home", home.getName()));
    }

}
