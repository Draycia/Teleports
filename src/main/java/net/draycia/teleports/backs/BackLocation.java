package net.draycia.teleports.backs;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class BackLocation {

    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    private UUID worldId;

    private transient Location location = null;

    public BackLocation(double x, double y, double z, float pitch, float yaw, UUID worldId) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.worldId = worldId;
    }

    public BackLocation(Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
        this.worldId = location.getWorld().getUID();

        this.location = location;
    }

    public Location getLocation() {
        if (location == null) {
            this.location = new Location(Bukkit.getWorld(getWorldId()), getX(), getY(), getZ(), getPitch(), getYaw());
        }

        return location;
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

    public UUID getWorldId() {
        return worldId;
    }
}
