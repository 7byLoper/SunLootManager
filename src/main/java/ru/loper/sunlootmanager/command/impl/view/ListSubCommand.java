package ru.loper.sunlootmanager.command.impl.view;

import java.util.ArrayList;
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
@SubCommandRegister(permission = "lootmanager.command.list", aliases = "list")
public class ListSubCommand implements BuildableCommand {
    private final LootManager lootManager;
    private final Messages messages;

    @Override
    public void handle(@NotNull CommandSender commandSender, String[] args) {
        List<String> lootNames = new ArrayList<>(lootManager.getLootNames());

        if (lootNames.isEmpty()) {
            messages.sendMessage(commandSender, messages.getLootListEmptyMessage());
            return;
        }

        messages.sendMessage(
                commandSender,
                messages.getLootListHeaderMessage().replace("{count}", String.valueOf(lootNames.size())));
        for (int i = 0; i < lootNames.size(); i++) {
            messages.sendMessage(
                    commandSender,
                    messages.getLootListEntryMessage()
                            .replace("{number}", String.valueOf(i + 1))
                            .replace("{name}", lootNames.get(i)));
        }
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender commandSender, String[] args) {
        return Collections.emptyList();
    }
}
