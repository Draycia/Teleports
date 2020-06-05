package net.draycia.teleports.commands.pwarps;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.draycia.teleports.Teleports;
import net.draycia.teleports.playerwarps.PlayerWarp;
import net.draycia.teleports.playerwarps.PlayerWarpRowRenderer;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.adapter.bukkit.TextAdapter;
import net.kyori.text.feature.pagination.Pagination;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("playerwarps|pwarps")
@CommandPermission("teleports.pwarps.list")
public class PlayerWarpsCommand extends BaseCommand {

    private Teleports main;

    public PlayerWarpsCommand(Teleports main) {
        this.main = main;
    }

    @Default
    @CommandCompletion("<page>")
    public void baseCommand(Player player, @Optional Integer page) {
        Pagination.Builder builder = Pagination.builder().width(53);

        Pagination<PlayerWarp> pagination = builder.build(
                TextComponent.of("Player Warps"),
                new PlayerWarpRowRenderer(main, player),
                (value) -> "/teleports:pwarps " + value
        );

        List<Component> components = pagination.render(main.getPlayerWarpManager().getPlayerWarps(),
                page == null ? 1 : page);

        for (Component component : components) {
            TextAdapter.sendMessage(player, component);
        }
    }

}
