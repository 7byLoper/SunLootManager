package ru.loper.sunlootmanager.commands.impl;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.loper.suncore.api.command.BuildableCommand;
import ru.loper.suncore.api.command.register.SubCommandRegister;
import ru.loper.sunlootmanager.api.manager.LootManager;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@SubCommandRegister(permission = "lootmanager.command.randomprice", aliases = "randomprice")
public class RandomPriceSubCommand implements BuildableCommand {
    private final LootManager lootManager;

    @Override
    public void handle(@NotNull CommandSender commandSender, String[] args) {
        lootManager.getAllLoots()
                .forEach(loot ->
                        loot.getItems().forEach(lootItem ->
                                lootItem.setValue("price", String.valueOf(ThreadLocalRandom.current().nextInt(10, 100)))));
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender commandSender, String[] args) {
        return List.of();
    }
}