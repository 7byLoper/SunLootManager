package ru.loper.sunlootmanager.commands.impl;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import ru.loper.suncore.api.command.SubCommand;
import ru.loper.suncore.utils.Colorize;
import ru.loper.sunlootmanager.api.manager.LootManager;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class CreateSubCommand implements SubCommand {
    private final LootManager lootManager;

    @Override
    public void onCommand(CommandSender commandSender, String[] args) {
        if (args.length < 2) {
            commandSender.sendMessage(Colorize.parse("&#FF5555▶ &7Использование: &f/loot create <название>"));
            return;
        }

        if (!lootManager.createLoot(args[1])) {
            commandSender.sendMessage(Colorize.parse("&#FF5555▶ &fЛут с таким названием &7уже существует&f!"));
            return;
        }

        commandSender.sendMessage(Colorize.parse("&#55FF55▶ &fЛут &#55FF55" + args[1] + " &fуспешно создан!"));
    }

    @Override
    public List<String> onTabCompleter(CommandSender commandSender, String[] args) {
        if (args.length == 2) {
            return Stream.of("<название>")
                    .filter(line -> line.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        }
        return Collections.emptyList();
    }
}