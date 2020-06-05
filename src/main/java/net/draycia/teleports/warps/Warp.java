package net.draycia.teleports.warps;

import net.draycia.teleports.SerializableLocation;
import org.bukkit.Location;

import java.util.UUID;

public class Warp {

    private SerializableLocation location;
    private UUID creator;
    private double price;
    private boolean enabled;
    private String name;

    public Warp(Location location, UUID creator, double price, boolean enabled, String name) {
        this.location = new SerializableLocation(location);
        this.creator = creator;
        this.price = price;
        this.enabled = enabled;
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public UUID getCreator() {
        return creator;
    }

    public Location getLocation() {
        return location.getLocation();
    }

    public void setLocation(Location location) {
        this.location = new SerializableLocation(location);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
