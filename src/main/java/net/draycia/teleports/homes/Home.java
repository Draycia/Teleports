package net.draycia.teleports.homes;

import net.draycia.teleports.SerializableLocation;
import org.bukkit.Location;

import java.util.UUID;

public class Home {

    private String name;
    private UUID owner;
    private SerializableLocation location;

    public Home(String name, UUID owner, SerializableLocation location) {
        this.name = name;
        this.owner = owner;
        this.location = location;
    }

    public Location getLocation() {
        return location.getLocation();
    }

    public UUID getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public void setLocation(Location location) {
        this.location = new SerializableLocation(location);
    }
}
