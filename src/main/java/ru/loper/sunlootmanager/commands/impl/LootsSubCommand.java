package ru.loper.sunlootmanager.commands.impl;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.loper.suncore.api.command.BuildableCommand;
import ru.loper.suncore.api.command.register.SubCommandRegister;
import ru.loper.sunlootmanager.api.manager.LootManager;
import ru.loper.sunlootmanager.config.LootConfigManager;
import ru.loper.sunlootmanager.menu.LootsMenu;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@SubCommandRegister(permission = "lootmanager.command.loots", aliases = "loots")
public class LootsSubCommand implements BuildableCommand {
    private final LootManager lootManager;
    private final LootConfigManager configManager;

    @Override
    public void handle(@NotNull CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(configManager.getPlayerOnlyMessage());
            return;
        }

        commandSender.sendMessage(configManager.getOpeningLootsMenuMessage());
        new LootsMenu(lootManager, configManager).show(player);
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender commandSender, String[] args) {
        return Collections.emptyList();
    }
}