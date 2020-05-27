package net.draycia.teleports.playerwarps;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerWarp {

    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    private transient Location location = null;

    private UUID worldId;

    private String name;

    private boolean isPublic;
    private double price;

    private UUID owner;
    private ArrayList<UUID> members;

    private UUID warpId;

    public PlayerWarp(double x, double y, double z, float pitch, float yaw, UUID worldId, String name, boolean isPublic, double price, UUID owner, ArrayList<UUID> members) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.worldId = worldId;
        this.name = name;
        this.isPublic = isPublic;
        this.price = price;
        this.owner = owner;
        this.members = members;

        this.location = new Location(Bukkit.getWorld(getWorldId()), getX(), getY(), getZ(), getPitch(), getYaw());
        this.warpId = UUID.randomUUID();
    }

    public PlayerWarp(Location location, String name, boolean isPublic, double price, UUID owner, ArrayList<UUID> members) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
        this.worldId = location.getWorld().getUID();
        this.name = name;
        this.isPublic = isPublic;
        this.price = price;
        this.owner = owner;
        this.members = members;

        this.location = location;
        this.warpId = UUID.randomUUID();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public Location getLocation() {
        if (location == null) {
            this.location = new Location(Bukkit.getWorld(getWorldId()), getX(), getY(), getZ(), getPitch(), getYaw());
        }

        return location;
    }

    public UUID getWorldId() {
        return worldId;
    }

    public World getWorld() {
        return Bukkit.getWorld(getWorldId());
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
}
