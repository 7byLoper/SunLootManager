package ru.loper.sunlootmanager.commands.impl;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import ru.loper.suncore.api.command.SubCommand;
import ru.loper.suncore.utils.Colorize;
import ru.loper.sunlootmanager.config.LootConfigManager;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class ReloadSubCommand implements SubCommand {
    private final LootConfigManager configManager;

    @Override
    public void onCommand(CommandSender commandSender, String[] args) {
        configManager.reloadAll();
        commandSender.sendMessage(Colorize.parse("&#55FF55▶ &fКонфигурация плагина успешно &7перезагружена&f!"));
    }

    @Override
    public List<String> onTabCompleter(CommandSender commandSender, String[] args) {
        return Collections.emptyList();
    }
}