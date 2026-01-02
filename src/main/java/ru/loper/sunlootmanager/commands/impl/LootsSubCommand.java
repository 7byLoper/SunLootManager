package ru.loper.sunlootmanager.commands.impl;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.loper.suncore.api.command.SubCommand;
import ru.loper.suncore.utils.Colorize;
import ru.loper.sunlootmanager.api.manager.LootManager;
import ru.loper.sunlootmanager.config.LootConfigManager;
import ru.loper.sunlootmanager.menu.LootsMenu;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class LootsSubCommand implements SubCommand {
    private final LootManager lootManager;
    private final LootConfigManager configManager;

    @Override
    public void onCommand(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(Colorize.parse("&#FF5555▶ &fДанная команда доступна только &7игрокам&f!"));
            return;
        }

        commandSender.sendMessage(Colorize.parse("&#55FF55▶ &fОткрытие меню лутов..."));
        new LootsMenu(lootManager, configManager).show(player);
    }

    @Override
    public List<String> onTabCompleter(CommandSender commandSender, String[] args) {
        return Collections.emptyList();
    }
}