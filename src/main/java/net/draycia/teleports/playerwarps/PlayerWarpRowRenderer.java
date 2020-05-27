package net.draycia.teleports.playerwarps;

import net.draycia.teleports.Teleports;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.feature.pagination.Pagination.Renderer.RowRenderer;
import net.kyori.text.format.TextColor;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Collections;

public class PlayerWarpRowRenderer implements RowRenderer<PlayerWarp> {

    private Teleports main;
    private Player player;

    public PlayerWarpRowRenderer(Teleports main, Player player) {
        this.main = main;
        this.player = player;
    }

    @Override
    public @NonNull Collection<Component> renderRow(@Nullable PlayerWarp value, int index) {
        if (value == null) {
            return Collections.emptyList();
        }

        boolean canUse = main.getPlayerWarpManager().canUsePlayerWarp(player, value);

        if (!canUse && !player.hasPermission("teleports.pwarp.list.seeall")) {
            return Collections.emptyList();
        }

        // TODO: language.yml this
        TextColor nameColor = canUse ? TextColor.GREEN : TextColor.RED;

        // TODO: language.yml this
        // TODO: language.yml this
        // TODO: language.yml this
        // TODO: language.yml this
        // TODO: language.yml this
        // TODO: language.yml this
        TextComponent.Builder component = TextComponent.builder()
                .append(TextComponent.of(index))
                .append(TextComponent.of(" > ").color(TextColor.GOLD))
                .append(TextComponent.of(value.getName()).color(nameColor))
                .append(TextComponent.of(" - ").color(TextColor.GRAY))
                .append(TextComponent.of(player.getName()).color(TextColor.AQUA))
                .clickEvent(ClickEvent.runCommand("/teleports:pwarp " + value.getName()));


        return Collections.singleton(component.build());
    }

}
