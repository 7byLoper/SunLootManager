package ru.loper.sunlootmanager.command.impl.storage;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.loper.suncore.api.command.BuildableCommand;
import ru.loper.suncore.api.command.register.SubCommandRegister;
import ru.loper.sunlootmanager.api.manager.LootManager;
import ru.loper.sunlootmanager.config.message.Messages;

@RequiredArgsConstructor
@SubCommandRegister(permission = "lootmanager.command.storage", aliases = "storage")
public class StorageSubCommand implements BuildableCommand {
    private final LootManager lootManager;
    private final Messages messages;

    @Override
    public void handle(@NotNull CommandSender commandSender, String[] args) {
        if (args.length < 2) {
            sendUsage(commandSender);
            return;
        }

        switch (args[1].toLowerCase(Locale.ROOT)) {
            case "save" -> {
                lootManager.saveAll();
                messages.sendMessage(commandSender, messages.getStorageSavedMessage());
            }
            case "reload" -> {
                lootManager.reload();
                messages.sendMessage(commandSender, messages.getStorageReloadedMessage());
            }
            default -> sendUsage(commandSender);
        }
    }

    private void sendUsage(CommandSender commandSender) {
        messages.sendMessage(commandSender, messages.getUsageStorageMessage());
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender commandSender, String[] args) {
        if (args.length == 2) {
            return Stream.of("save", "reload")
                    .filter(action -> action.startsWith(args[1].toLowerCase(Locale.ROOT)))
                    .toList();
        }

        return Collections.emptyList();
    }
}
