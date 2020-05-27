package net.draycia.teleports.listeners;

import net.draycia.teleports.Teleports;
import net.draycia.teleports.backs.BackLocation;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportListener implements Listener {

    private Teleports main;

    public TeleportListener(Teleports main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        if (main == null) {
            System.out.println("main null");
        }

        if (main.getBackLocations() == null) {
            System.out.println("back null");
        }

        if (event.getPlayer() == null) {
            System.out.println("player null");
        }

        main.getBackLocations().put(event.getPlayer().getUniqueId(), new BackLocation(event.getFrom()));
    }

}
