package net.draycia.teleports.commands.misc;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import net.draycia.teleports.Teleports;
import net.kyori.text.adapter.bukkit.TextAdapter;
import org.bukkit.command.CommandSender;

@CommandAlias("tpreload")
@CommandPermission("teleports.reload")
public class TeleportReloadCommand extends BaseCommand {

    private Teleports main;

    public TeleportReloadCommand(Teleports main) {
        this.main = main;
    }

    @Default
    public void baseCommand(CommandSender commandSender) {
        main.reloadConfig();
        main.reloadLanguage();

        TextAdapter.sendMessage(commandSender, main.getMessage("reload"));
    }

}
