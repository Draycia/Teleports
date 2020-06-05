package net.draycia.teleports.commands.pwarps;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.draycia.teleports.Teleports;
import net.draycia.teleports.playerwarps.PlayerWarp;
import net.kyori.text.adapter.bukkit.TextAdapter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@CommandAlias("playereditwarp|peditwarp|pewarp|pew")
@CommandPermission("teleports.pwarps.edit")
public class PlayerEditWarpCommand extends BaseCommand {

    private Teleports main;

    public PlayerEditWarpCommand(Teleports main) {
        this.main = main;
    }

    @Default
    public void baseCommand(Player player) {
        player.sendMessage("/peditwarp <setcost|setpublic|setlocation|addmember|removemember>");
    }

    @CommandCompletion("@player-warp-owned price")
    @Subcommand("setcost")
    public void setCost(Player player, @Conditions("pwarp-exists:true,pwarp-owner:true") PlayerWarp playerWarp, float price) {
        playerWarp.setPrice(price);
        TextAdapter.sendMessage(player, main.getMessage("pwarp-set-price",
                "pwarp", playerWarp.getName(), "price", Teleports.FORMAT.format(price)));
    }

    @CommandCompletion("@player-warp-owned @boolean")
    @Subcommand("setpublic")
    public void setPublic(Player player, @Conditions("pwarp-exists:true,pwarp-owner:true") PlayerWarp playerWarp, @Default("true") Boolean isPublic) {
        playerWarp.setPublic(isPublic);

        if (isPublic) {
            TextAdapter.sendMessage(player, main.getMessage("pwarp-set-public-on",
                    "pwarp", playerWarp.getName()));
        } else {
            TextAdapter.sendMessage(player, main.getMessage("pwarp-set-public-off",
                    "pwarp", playerWarp.getName()));
        }
    }

    @CommandCompletion("@player-warp-owned @players")
    @Subcommand("addmember")
    public void addMember(Player player, @Conditions("pwarp-exists:true,pwarp-owner:true") PlayerWarp playerWarp, Player target) {
        playerWarp.getMembers().add(target.getUniqueId());
        TextAdapter.sendMessage(player, main.getMessage("pwarp-add-member", "target",
                target.getName(), "pwarp", playerWarp.getName()));
    }

    @CommandCompletion("@player-warp-owned @player-warp-members")
    @Subcommand("removemember")
    public void removeMember(Player player, @Conditions("pwarp-exists:true,pwarp-owner:true") PlayerWarp playerWarp, Player target) {
        playerWarp.getMembers().remove(target.getUniqueId());
        TextAdapter.sendMessage(player, main.getMessage("pwarp-remove-member", "target",
                target.getName(), "pwarp", playerWarp.getName()));
    }

    @CommandCompletion("@player-warp-owned price")
    @Subcommand("setlocation")
    public void setLocation(Player player, @Conditions("pwarp-exists:true,pwarp-owner:true") PlayerWarp playerWarp) {
        Location location = player.getLocation();

        playerWarp.setLocation(player.getLocation());

        TextAdapter.sendMessage(player, main.getMessage("pwarp-set-location",
                "x", Teleports.FORMAT.format(location.getX()),
                "y", Teleports.FORMAT.format(location.getY()),
                "z", Teleports.FORMAT.format(location.getZ()),
                "world", location.getWorld().getName(),
                "pitch", Teleports.FORMAT.format(location.getPitch()),
                "yaw", Teleports.FORMAT.format(location.getYaw()),
                "pwarp", playerWarp.getName()));
    }

}
