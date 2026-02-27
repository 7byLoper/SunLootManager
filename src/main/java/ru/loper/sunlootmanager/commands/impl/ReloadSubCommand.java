package ru.loper.sunlootmanager.commands.impl;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.loper.suncore.api.command.BuildableCommand;
import ru.loper.suncore.api.command.register.SubCommandRegister;
import ru.loper.sunlootmanager.config.LootConfigManager;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@SubCommandRegister(permission = "lootmanager.command.reload", aliases = "reload")
public class ReloadSubCommand implements BuildableCommand {
    private final LootConfigManager configManager;

    @Override
    public void handle(@NotNull CommandSender commandSender, String[] args) {
        configManager.reloadAll();
        commandSender.sendMessage(configManager.getConfigReloadedMessage());
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender commandSender, String[] args) {
        return Collections.emptyList();
    }
}