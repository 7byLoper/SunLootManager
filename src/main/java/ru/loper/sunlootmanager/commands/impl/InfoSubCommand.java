package ru.loper.sunlootmanager.commands.impl;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.loper.suncore.api.command.BuildableCommand;
import ru.loper.suncore.api.command.register.SubCommandRegister;
import ru.loper.sunlootmanager.api.manager.LootManager;
import ru.loper.sunlootmanager.api.modules.Loot;
import ru.loper.sunlootmanager.config.LootConfigManager;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@SubCommandRegister(permission = "lootmanager.command.info", aliases = "info")
public class InfoSubCommand implements BuildableCommand {
    private final LootManager lootManager;
    private final LootConfigManager configManager;

    @Override
    public void handle(@NotNull CommandSender commandSender, String[] args) {
        if (args.length < 2) {
            commandSender.sendMessage(configManager.getUsageInfoMessage());
            return;
        }

        Optional<Loot> optionalLoot = lootManager.getLoot(args[1]);
        if (optionalLoot.isEmpty()) {
            commandSender.sendMessage(configManager.getLootNotFoundMessage());
            return;
        }

        Loot loot = optionalLoot.get();
        commandSender.sendMessage(configManager.getLootInfoHeaderMessage());
        commandSender.sendMessage(configManager.getLootInfoNameMessage().replace("{name}", loot.getName()));
        commandSender.sendMessage(configManager.getLootInfoItemsMessage().replace("{count}", String.valueOf(loot.getItems().size())));
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