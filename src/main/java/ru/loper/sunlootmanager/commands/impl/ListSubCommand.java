package ru.loper.sunlootmanager.commands.impl;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import ru.loper.suncore.api.command.SubCommand;
import ru.loper.suncore.utils.Colorize;
import ru.loper.sunlootmanager.api.manager.LootManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class ListSubCommand implements SubCommand {
    private final LootManager lootManager;

    @Override
    public void onCommand(CommandSender commandSender, String[] args) {
        List<String> lootNames = new ArrayList<>(lootManager.getLootNames());

        if (lootNames.isEmpty()) {
            commandSender.sendMessage(Colorize.parse("&#FFAA00▶ &fСозданные луты &7отсутствуют&f!"));
            return;
        }

        commandSender.sendMessage(Colorize.parse("&#55FF55▶ &aСписок лутов &7(" + lootNames.size() + "):"));
        for (int i = 0; i < lootNames.size(); i++) {
            commandSender.sendMessage(Colorize.parse("&#55FFFF" + (i + 1) + ". &7" + lootNames.get(i)));
        }
    }

    @Override
    public List<String> onTabCompleter(CommandSender commandSender, String[] args) {
        return Collections.emptyList();
    }
}