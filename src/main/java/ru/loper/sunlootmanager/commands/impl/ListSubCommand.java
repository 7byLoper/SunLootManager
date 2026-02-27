package ru.loper.sunlootmanager.commands.impl;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.loper.suncore.api.command.BuildableCommand;
import ru.loper.suncore.api.command.register.SubCommandRegister;
import ru.loper.sunlootmanager.api.manager.LootManager;
import ru.loper.sunlootmanager.config.LootConfigManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@SubCommandRegister(permission = "lootmanager.command.list", aliases = "list")
public class ListSubCommand implements BuildableCommand {
    private final LootManager lootManager;
    private final LootConfigManager configManager;

    @Override
    public void handle(@NotNull CommandSender commandSender, String[] args) {
        List<String> lootNames = new ArrayList<>(lootManager.getLootNames());

        if (lootNames.isEmpty()) {
            commandSender.sendMessage(configManager.getLootListEmptyMessage());
            return;
        }

        commandSender.sendMessage(configManager.getLootListHeaderMessage().replace("{count}", String.valueOf(lootNames.size())));
        for (int i = 0; i < lootNames.size(); i++) {
            commandSender.sendMessage(configManager.getLootListEntryMessage()
                    .replace("{number}", String.valueOf(i + 1))
                    .replace("{name}", lootNames.get(i)));
        }
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender commandSender, String[] args) {
        return Collections.emptyList();
    }
}