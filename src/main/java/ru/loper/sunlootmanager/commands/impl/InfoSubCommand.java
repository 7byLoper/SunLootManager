package ru.loper.sunlootmanager.commands.impl;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import ru.loper.suncore.api.command.SubCommand;
import ru.loper.sunlootmanager.api.manager.LootManager;
import ru.loper.sunlootmanager.api.modules.Loot;
import ru.loper.suncore.utils.Colorize;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class InfoSubCommand implements SubCommand {
    private final LootManager lootManager;

    @Override
    public void onCommand(CommandSender commandSender, String[] args) {
        if (args.length < 2) {
            commandSender.sendMessage(Colorize.parse("&#FF5555▶ &7Использование: &f/loot info <название>"));
            return;
        }

        Optional<Loot> optionalLoot = lootManager.getLoot(args[1]);
        if (optionalLoot.isEmpty()) {
            commandSender.sendMessage(Colorize.parse("&#FF5555▶ &fЛут с таким названием &7отсутствует&f!"));
            return;
        }

        Loot loot = optionalLoot.get();
        commandSender.sendMessage(Colorize.parse("&#FFAA00▶ &6Информация о луте:"));
        commandSender.sendMessage(Colorize.parse("&#55FFFF▪ &fНазвание: &7" + loot.getName()));
        commandSender.sendMessage(Colorize.parse("&#55FFFF▪ &fПредметов: &7" + loot.getItems().size()));
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
