package ru.loper.sunlootmanager.commands.impl;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import ru.loper.suncore.api.command.SubCommand;
import ru.loper.sunlootmanager.api.manager.LootManager;
import ru.loper.suncore.utils.Colorize;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class RemoveSubCommand implements SubCommand {
    private final LootManager lootManager;

    @Override
    public void onCommand(CommandSender commandSender, String[] args) {
        if (args.length < 2) {
            commandSender.sendMessage(Colorize.parse("&#FF5555▶ &7Использование: &f/loot remove <название>"));
            return;
        }

        if (!lootManager.deleteLoot(args[1])) {
            commandSender.sendMessage(Colorize.parse("&#FF5555▶ &fЛута с таким названием &7не существует&f!"));
            return;
        }

        commandSender.sendMessage(Colorize.parse("&#55FF55▶ &fЛут &#55FF55" + args[1] + " &fуспешно удален!"));
    }

    @Override
    public List<String> onTabCompleter(CommandSender commandSender, String[] args) {
        if (args.length == 2) {
            return lootManager.getLootNames().stream()
                    .filter(line -> line.toLowerCase().startsWith(args[2].toLowerCase()))
                    .toList();
        }
        return Collections.emptyList();
    }
}