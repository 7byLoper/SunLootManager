package ru.loper.sunlootmanager.commands.impl;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.loper.suncore.api.command.BuildableCommand;
import ru.loper.suncore.api.command.register.SubCommandRegister;
import ru.loper.sunlootmanager.api.manager.LootManager;
import ru.loper.sunlootmanager.api.modules.Loot;
import ru.loper.sunlootmanager.config.LootConfigManager;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@SubCommandRegister(permission = "lootmanager.command.give", aliases = "give")
public class GiveSubCommand implements BuildableCommand {
    private final LootManager lootManager;
    private final LootConfigManager configManager;

    @Override
    public void handle(@NotNull CommandSender commandSender, String[] args) {
        if (args.length < 4) {
            commandSender.sendMessage(configManager.getUsageGiveMessage());
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            commandSender.sendMessage(configManager.getPlayerNotFoundMessage().replace("{player}", args[1]));
            return;
        }

        Optional<Loot> optionalLoot = lootManager.getLoot(args[2]);
        if (optionalLoot.isEmpty()) {
            commandSender.sendMessage(configManager.getLootNotFoundGiveMessage());
            return;
        }

        try {
            int amount = Integer.parseInt(args[3]);
            Loot loot = optionalLoot.get();

            loot.generateLoot(amount).forEach(item -> target.getInventory().addItem(item));

            commandSender.sendMessage(configManager.getGiveSuccessMessage()
                    .replace("{loot}", loot.getName())
                    .replace("{player}", target.getName()));
            target.sendMessage(configManager.getGiveReceivedMessage().replace("{loot}", loot.getName()));
        } catch (NumberFormatException e) {
            commandSender.sendMessage(configManager.getInvalidNumberFormatMessage().replace("{number}", args[3]));
        }
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender commandSender, String[] args) {
        if (args.length == 2) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        } else if (args.length == 3) {
            return lootManager.getLootNames().stream()
                    .filter(line -> line.toLowerCase().startsWith(args[2].toLowerCase()))
                    .toList();
        } else if (args.length == 4) {
            return Stream.of("1", "8", "32", "64")
                    .filter(line -> line.toLowerCase().startsWith(args[3].toLowerCase()))
                    .toList();
        }
        return Collections.emptyList();
    }
}