package net.draycia.teleports.listeners;

import net.draycia.teleports.Teleports;
import net.draycia.teleports.SerializableLocation;
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
        main.getBackLocations().put(event.getPlayer().getUniqueId(), new SerializableLocation(event.getFrom()));
    }

}
