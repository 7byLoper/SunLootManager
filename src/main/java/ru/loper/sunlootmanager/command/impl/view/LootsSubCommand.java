package ru.loper.sunlootmanager.command.impl.view;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.loper.suncore.api.command.BuildableCommand;
import ru.loper.suncore.api.command.register.SubCommandRegister;
import ru.loper.sunlootmanager.api.manager.LootManager;
import ru.loper.sunlootmanager.config.LootConfigManager;
import ru.loper.sunlootmanager.config.message.Messages;
import ru.loper.sunlootmanager.menu.LootsMenu;

@RequiredArgsConstructor
@SubCommandRegister(permission = "lootmanager.command.loots", aliases = "loots")
public class LootsSubCommand implements BuildableCommand {
    private final LootManager lootManager;
    private final LootConfigManager configManager;
    private final Messages messages;

    @Override
    public void handle(@NotNull CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player)) {
            messages.sendMessage(commandSender, messages.getPlayerOnlyMessage());
            return;
        }

        messages.sendMessage(commandSender, messages.getOpeningLootsMenuMessage());
        new LootsMenu(lootManager, configManager).show(player);
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender commandSender, String[] args) {
        return Collections.emptyList();
    }
}
