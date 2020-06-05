package net.draycia.teleports.playerwarps;

import net.draycia.teleports.SerializableLocation;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerWarp {

    private SerializableLocation location;

    private String name;

    private boolean isPublic;
    private double price;

    private UUID owner;
    private ArrayList<UUID> members;

    private UUID warpId;

    public PlayerWarp(Location location, String name, boolean isPublic, double price, UUID owner, ArrayList<UUID> members) {

        this.name = name;
        this.isPublic = isPublic;
        this.price = price;
        this.owner = owner;
        this.members = members;

        this.location = new SerializableLocation(location);
        this.warpId = UUID.randomUUID();
    }

    public Location getLocation() {
        return location.getLocation();
    }

    public String getName() {
        return name;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public double getPrice() {
        return price;
    }

    public UUID getOwner() {
        return owner;
    }

    public ArrayList<UUID> getMembers() {
        return members;
    }

    public UUID getWarpId() {
        return warpId;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void setLocation(Location location) {
        this.location = new SerializableLocation(location);
    }
}
