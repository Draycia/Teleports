package net.draycia.teleports.commands.homes;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.draycia.teleports.Teleports;
import net.draycia.teleports.homes.Home;
import net.kyori.text.adapter.bukkit.TextAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("delhome|deletehome")
@CommandPermission("teleports.homes.delete")
public class DeleteHomeCommand extends BaseCommand {

    private Teleports main;

    public DeleteHomeCommand(Teleports main) {
        this.main = main;
    }

    @Default
    @CommandCompletion("@homes")
    public void baseCommand(Player player, @Optional @Conditions("home-exists:true,can-use-home:true") Home home) {
        main.getHomeManager().removeHome(Bukkit.getOfflinePlayer(home.getOwner()), home);
        TextAdapter.sendMessage(player, main.getMessage("homes-deleted", "home", home.getName()));
    }

}
