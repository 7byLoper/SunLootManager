package ru.loper.sunlootmanager.commands.impl;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.loper.suncore.api.command.SubCommand;
import ru.loper.sunlootmanager.api.manager.LootManager;
import ru.loper.sunlootmanager.api.modules.Loot;
import ru.loper.sunlootmanager.config.LootConfigManager;
import ru.loper.sunlootmanager.menu.LootItemsMenu;
import ru.loper.suncore.utils.Colorize;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class EditSubCommand implements SubCommand {
    private final LootManager lootManager;
    private final LootConfigManager configManager;

    @Override
    public void onCommand(CommandSender commandSender, String[] args) {
        if (args.length < 2) {
            commandSender.sendMessage(Colorize.parse("&#FF5555▶ &7Использование: &f/loot edit <название>"));
            return;
        }

        Optional<Loot> optionalLoot = lootManager.getLoot(args[1]);
        if (optionalLoot.isEmpty()) {
            commandSender.sendMessage(Colorize.parse("&#FF5555▶ &fЛут с таким названием &7отсутствует&f!"));
            return;
        }

        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(Colorize.parse("&#FF5555▶ &fДанная команда доступна только &7игрокам&f!"));
            return;
        }

        commandSender.sendMessage(Colorize.parse("&#55FF55▶ &fОткрытие редактора лута &#55FF55" + args[1]));
        new LootItemsMenu(optionalLoot.get(), null, configManager).show(player);
    }

    @Override
    public List<String> onTabCompleter(CommandSender commandSender, String[] args) {
        if (args.length == 2) {
            return lootManager.getLootNames().stream()
                    .filter(line -> line.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        }
        return Collections.emptyList();
    }
}