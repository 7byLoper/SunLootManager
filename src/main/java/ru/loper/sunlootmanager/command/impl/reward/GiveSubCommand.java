package ru.loper.sunlootmanager.command.impl.reward;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.loper.suncore.api.command.BuildableCommand;
import ru.loper.suncore.api.command.register.SubCommandRegister;
import ru.loper.sunlootmanager.api.manager.LootManager;
import ru.loper.sunlootmanager.api.modules.Loot;
import ru.loper.sunlootmanager.api.modules.LootItem;
import ru.loper.sunlootmanager.config.message.Messages;

@RequiredArgsConstructor
@SubCommandRegister(permission = "lootmanager.command.give", aliases = "give")
public class GiveSubCommand implements BuildableCommand {
    private final LootManager lootManager;
    private final Messages messages;

    @Override
    public void handle(@NotNull CommandSender commandSender, String[] args) {
        if (args.length < 4) {
            messages.sendMessage(commandSender, messages.getUsageGiveMessage());
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            messages.sendMessage(
                    commandSender, messages.getPlayerNotFoundMessage().replace("{player}", args[1]));
            return;
        }

        Optional<Loot> optionalLoot = lootManager.getLoot(args[2]);
        if (optionalLoot.isEmpty()) {
            messages.sendMessage(commandSender, messages.getLootNotFoundGiveMessage());
            return;
        }

        try {
            int amount = Integer.parseInt(args[3]);
            Loot loot = optionalLoot.get();

            loot.generateLootItems(amount).forEach(item -> giveLootItem(target, item));

            messages.sendMessage(
                    commandSender,
                    messages.getGiveSuccessMessage()
                            .replace("{loot}", loot.getName())
                            .replace("{player}", target.getName()));
            messages.sendMessage(target, messages.getGiveReceivedMessage().replace("{loot}", loot.getName()));
        } catch (NumberFormatException e) {
            messages.sendMessage(
                    commandSender, messages.getInvalidNumberFormatMessage().replace("{number}", args[3]));
        }
    }

    private void giveLootItem(Player target, LootItem lootItem) {
        target.getInventory().addItem(lootItem.getItemStackRandomAmount());

        String command = lootItem.getValue("command");
        if (command == null || command.isBlank()) {
            return;
        }

        String parsedCommand = command.replace("{player}", target.getName()).trim();
        if (parsedCommand.startsWith("/")) {
            parsedCommand = parsedCommand.substring(1);
        }

        if (!parsedCommand.isBlank()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parsedCommand);
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
