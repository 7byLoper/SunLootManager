package ru.loper.sunlootmanager.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.loper.suncore.api.command.executor.BaseCommandExecutor;
import ru.loper.suncore.api.command.register.CommandRegister;
import ru.loper.sunlootmanager.SunLootManager;
import ru.loper.sunlootmanager.command.impl.management.*;
import ru.loper.sunlootmanager.command.impl.reward.GiveSubCommand;
import ru.loper.sunlootmanager.command.impl.storage.ImportSubCommand;
import ru.loper.sunlootmanager.command.impl.storage.StorageSubCommand;
import ru.loper.sunlootmanager.command.impl.view.*;

@CommandRegister(name = "lootmanager", permission = "lootmanager.command.use", aliases = "loot")
public class LootCommand extends BaseCommandExecutor {
    private final SunLootManager plugin;

    public LootCommand(SunLootManager plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public String getNoPermissionMessage() {
        return plugin.getConfigManager().getMessages().getNoPermissionsMessage();
    }

    @Override
    public void registerWrappers() {
        addSubCommand(new CreateSubCommand(
                plugin.getLootManager(), plugin.getConfigManager().getMessages()));
        addSubCommand(new EditSubCommand(
                plugin.getLootManager(),
                plugin.getConfigManager(),
                plugin.getConfigManager().getMessages()));
        addSubCommand(new RemoveSubCommand(
                plugin.getLootManager(), plugin.getConfigManager().getMessages()));
        addSubCommand(new LootsSubCommand(
                plugin.getLootManager(),
                plugin.getConfigManager(),
                plugin.getConfigManager().getMessages()));
        addSubCommand(new InfoSubCommand(
                plugin.getLootManager(), plugin.getConfigManager().getMessages()));
        addSubCommand(new ListSubCommand(
                plugin.getLootManager(), plugin.getConfigManager().getMessages()));
        addSubCommand(new ImportSubCommand(
                plugin.getLootManager(), plugin.getConfigManager().getMessages()));
        addSubCommand(new StorageSubCommand(
                plugin.getLootManager(), plugin.getConfigManager().getMessages()));
        addSubCommand(new GiveSubCommand(
                plugin.getLootManager(), plugin.getConfigManager().getMessages()));
        addSubCommand(new SwapSubCommand(
                plugin.getLootManager(), plugin.getConfigManager().getMessages()));
        addSubCommand(new RandomPriceSubCommand(plugin.getLootManager()));
    }

    @Override
    public void handleNoArguments(@NotNull CommandSender commandSender) {}
}
