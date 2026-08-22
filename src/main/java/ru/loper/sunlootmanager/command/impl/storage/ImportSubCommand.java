package ru.loper.sunlootmanager.command.impl.storage;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.loper.suncore.api.command.BuildableCommand;
import ru.loper.suncore.api.command.register.SubCommandRegister;
import ru.loper.sunlootmanager.api.manager.LootManager;
import ru.loper.sunlootmanager.config.message.Messages;

@RequiredArgsConstructor
@SubCommandRegister(permission = "lootmanager.command.import", aliases = "import")
public class ImportSubCommand implements BuildableCommand {
    private final LootManager lootManager;
    private final Messages messages;

    @Override
    public void handle(@NotNull CommandSender commandSender, String[] args) {
        if (args.length < 2) {
            messages.sendMessage(commandSender, messages.getUsageImportMessage());
            return;
        }

        if (!lootManager.importLoot(args[1])) {
            messages.sendMessage(commandSender, messages.getLootNotFoundMessage());
            return;
        }

        messages.sendMessage(commandSender, messages.getLootImportedMessage().replace("{name}", args[1]));
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender commandSender, String[] args) {
        if (args.length == 2) {
            return lootManager.getStoredLootNames().stream()
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        }

        return Collections.emptyList();
    }
}
