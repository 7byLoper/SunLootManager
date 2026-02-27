package ru.loper.sunlootmanager.commands.impl;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.loper.suncore.api.command.BuildableCommand;
import ru.loper.suncore.api.command.register.SubCommandRegister;
import ru.loper.sunlootmanager.api.manager.LootManager;
import ru.loper.sunlootmanager.config.LootConfigManager;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@SubCommandRegister(permission = "lootmanager.command.remove", aliases = "remove")
public class RemoveSubCommand implements BuildableCommand {
    private final LootManager lootManager;
    private final LootConfigManager configManager;

    @Override
    public void handle(@NotNull CommandSender commandSender, String[] args) {
        if (args.length < 2) {
            commandSender.sendMessage(configManager.getUsageRemoveMessage());
            return;
        }

        if (!lootManager.deleteLoot(args[1])) {
            commandSender.sendMessage(configManager.getLootNotFoundMessage());
            return;
        }

        commandSender.sendMessage(configManager.getLootDeletedMessage().replace("{name}", args[1]));
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender commandSender, String[] args) {
        if (args.length == 2) {
            return lootManager.getLootNames().stream()
                    .filter(line -> line.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        }
        return Collections.emptyList();
    }
}