package ru.loper.sunlootmanager.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.loper.suncore.api.command.executor.BaseCommandExecutor;
import ru.loper.suncore.api.command.register.CommandRegister;
import ru.loper.sunlootmanager.SunLootManager;
import ru.loper.sunlootmanager.commands.impl.*;

@CommandRegister(name = "lootmanager", permission = "lootmanager.command.use")
public class LootCommand extends BaseCommandExecutor {
    private final SunLootManager plugin;

    public LootCommand(SunLootManager plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public String getNoPermissionMessage() {
        return plugin.getConfigManager().getNoPermissionsMessage();
    }

    @Override
    public void registerWrappers() {
        addSubCommand(new CreateSubCommand(plugin.getLootManager(), plugin.getConfigManager()));
        addSubCommand(new EditSubCommand(plugin.getLootManager(), plugin.getConfigManager()));
        addSubCommand(new RemoveSubCommand(plugin.getLootManager(), plugin.getConfigManager()));
        addSubCommand(new LootsSubCommand(plugin.getLootManager(), plugin.getConfigManager()));
        addSubCommand(new InfoSubCommand(plugin.getLootManager(), plugin.getConfigManager()));
        addSubCommand(new ListSubCommand(plugin.getLootManager(), plugin.getConfigManager()));
        addSubCommand(new ReloadSubCommand(plugin.getConfigManager()));
        addSubCommand(new GiveSubCommand(plugin.getLootManager(), plugin.getConfigManager()));
        addSubCommand(new SwapSubCommand(plugin.getLootManager(), plugin.getConfigManager()));
        addSubCommand(new RandomPriceSubCommand(plugin.getLootManager()));
    }

    @Override
    public void handleNoArguments(@NotNull CommandSender commandSender) {

    }
}