package net.draycia.teleports.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import net.draycia.teleports.Teleports;
import net.draycia.teleports.playerwarps.PlayerWarp;
import net.kyori.text.adapter.bukkit.TextAdapter;
import org.bukkit.entity.Player;

@CommandAlias("playereditwarp|peditwarp|pewarp|pew")
@CommandPermission("teleports.pwarps.edit")
public class PlayerEditWarpCommand extends BaseCommand {

    private Teleports main;

    public PlayerEditWarpCommand(Teleports main) {
        this.main = main;
    }

    @CommandCompletion("@player-warp-owned price")
    @Subcommand("setcost")
    public void setCost(Player player, PlayerWarp playerWarp, float price) {
        if (playerWarp == null) {
            TextAdapter.sendComponent(player, main.getMessage("player-warp-not-found"));
            return;
        }

        if (!playerWarp.getOwner().equals(player.getUniqueId())) {
            return;
        }

        playerWarp.setPrice(price);
    }

    @CommandCompletion("@player-warp-owned @boolean")
    @Subcommand("setpublic")
    public void setPublic(Player player, PlayerWarp playerWarp, boolean isPublic) {
        if (playerWarp == null) {
            TextAdapter.sendComponent(player, main.getMessage("player-warp-not-found"));
            return;
        }

        if (!playerWarp.getOwner().equals(player.getUniqueId())) {
            return;
        }

        playerWarp.setPublic(isPublic);
    }

}
