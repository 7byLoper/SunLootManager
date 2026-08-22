package ru.loper.sunlootmanager.command.impl.management;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.loper.suncore.api.command.BuildableCommand;
import ru.loper.suncore.api.command.register.SubCommandRegister;
import ru.loper.sunlootmanager.api.manager.LootManager;
import ru.loper.sunlootmanager.api.modules.Loot;
import ru.loper.sunlootmanager.config.LootConfigManager;
import ru.loper.sunlootmanager.config.message.Messages;
import ru.loper.sunlootmanager.menu.LootItemsMenu;

@RequiredArgsConstructor
@SubCommandRegister(permission = "lootmanager.command.edit", aliases = "edit")
public class EditSubCommand implements BuildableCommand {
    private final LootManager lootManager;
    private final LootConfigManager configManager;
    private final Messages messages;

    @Override
    public void handle(@NotNull CommandSender commandSender, String[] args) {
        if (args.length < 2) {
            messages.sendMessage(commandSender, messages.getUsageEditMessage());
            return;
        }

        Optional<Loot> optionalLoot = lootManager.getLoot(args[1]);
        if (optionalLoot.isEmpty()) {
            messages.sendMessage(commandSender, messages.getLootNotFoundMessage());
            return;
        }

        if (!(commandSender instanceof Player player)) {
            messages.sendMessage(commandSender, messages.getPlayerOnlyMessage());
            return;
        }

        messages.sendMessage(commandSender, messages.getEditingLootMessage().replace("{name}", args[1]));
        new LootItemsMenu(optionalLoot.get(), null, configManager).show(player);
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender commandSender, String[] args) {
        if (args.length == 2) {
            return lootManager.getLootNames().stream()
                    .filter(line -> line.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        }
        return Collections.emptyList();
    }
}
