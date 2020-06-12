package net.draycia.teleports.commands.misc;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import io.papermc.lib.PaperLib;
import net.draycia.teleports.Teleports;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("tpgh|tpghere|tpgrouphere")
public class TeleportGroupHereCommand extends BaseCommand {

    private Teleports main;

    public TeleportGroupHereCommand(Teleports main) {
        this.main = main;
    }

    @Default
    @CommandCompletion("PrimaryGroupOnly Group")
    public void baseCommand(Player player, boolean primaryGroupOnly, String group) {
        Permission permission = main.getPermission();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if ((primaryGroupOnly && permission.getPrimaryGroup(onlinePlayer).equalsIgnoreCase(group)) || permission.playerInGroup(onlinePlayer, group)) {
                PaperLib.teleportAsync(onlinePlayer, player.getLocation());
            }
        }
    }

}
