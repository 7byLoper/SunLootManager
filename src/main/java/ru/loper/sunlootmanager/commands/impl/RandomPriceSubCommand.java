package ru.loper.sunlootmanager.commands.impl;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import ru.loper.suncore.api.command.SubCommand;
import ru.loper.sunlootmanager.api.manager.LootManager;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class RandomPriceSubCommand implements SubCommand {
    private final LootManager lootManager;

    @Override
    public void onCommand(CommandSender commandSender, String[] args) {
        lootManager.getAllLoots()
                .forEach(loot ->
                        loot.getItems().forEach(lootItem ->
                                lootItem.setValue("price", String.valueOf(ThreadLocalRandom.current().nextInt(10, 100)))));
    }

    @Override
    public List<String> onTabCompleter(CommandSender commandSender, String[] args) {
        return List.of();
    }
}
