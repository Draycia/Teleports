package net.draycia.teleports.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.draycia.teleports.Teleports;
import net.draycia.teleports.playerwarps.PlayerWarp;
import net.kyori.text.adapter.bukkit.TextAdapter;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

@CommandAlias("playereditwarp|peditwarp|pewarp|pew")
@CommandPermission("teleports.pwarps.edit")
public class PlayerEditWarpCommand extends BaseCommand {

    private Teleports main;
    private DecimalFormat format = new DecimalFormat("#.##");

    public PlayerEditWarpCommand(Teleports main) {
        this.main = main;
    }

    @CommandCompletion("@player-warp-owned price")
    @Subcommand("setcost")
    public void setCost(Player player, @Conditions("pwarp-owner") PlayerWarp playerWarp, float price) {
        playerWarp.setPrice(price);
        TextAdapter.sendMessage(player, main.getMessage("pwarp-set-price",
                "pwarp", playerWarp.getName(), "price", format.format(price)));
    }

    @CommandCompletion("@player-warp-owned @boolean")
    @Subcommand("setpublic")
    public void setPublic(Player player, @Conditions("pwarp-owner") PlayerWarp playerWarp, boolean isPublic) {
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
    public void addMember(Player player, @Conditions("pwarp-owner") PlayerWarp playerWarp, Player target) {
        playerWarp.getMembers().add(target.getUniqueId());
        TextAdapter.sendMessage(player, main.getMessage("pwarp-add-member", "target",
                target.getName(), "pwarp", playerWarp.getName()));
    }

    @CommandCompletion("@player-warp-owned @player-warp-members")
    @Subcommand("removemember")
    public void removeMember(Player player, @Conditions("pwarp-owner") PlayerWarp playerWarp, Player target) {
        playerWarp.getMembers().remove(target.getUniqueId());
        TextAdapter.sendMessage(player, main.getMessage("pwarp-remove-member", "target",
                target.getName(), "pwarp", playerWarp.getName()));
    }

}
