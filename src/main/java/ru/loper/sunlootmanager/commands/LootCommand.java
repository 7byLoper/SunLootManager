package ru.loper.sunlootmanager.commands;

import org.bukkit.permissions.Permission;
import ru.loper.suncore.api.command.AdvancedSmartCommandExecutor;
import ru.loper.sunlootmanager.SunLootManager;
import ru.loper.sunlootmanager.commands.impl.*;
import ru.loper.sunlootmanager.config.LootConfigManager;

public class LootCommand extends AdvancedSmartCommandExecutor {
    private final LootConfigManager configManager;

    public LootCommand(SunLootManager plugin) {
        configManager = plugin.getConfigManager();

        addSubCommand(new CreateSubCommand(plugin.getLootManager()), new Permission("lootmanager.command.create"), "create");
        addSubCommand(new EditSubCommand(plugin.getLootManager(), plugin.getConfigManager()), new Permission("lootmanager.command.edit"), "edit");
        addSubCommand(new RemoveSubCommand(plugin.getLootManager()), new Permission("lootmanager.command.remove"), "remove");
        addSubCommand(new LootsSubCommand(plugin.getLootManager(), plugin.getConfigManager()), new Permission("lootmanager.command.loots"), "loots");
        addSubCommand(new InfoSubCommand(plugin.getLootManager()), new Permission("lootmanager.command.info"), "info");
        addSubCommand(new ListSubCommand(plugin.getLootManager()), new Permission("lootmanager.command.list"), "list");
        addSubCommand(new ReloadSubCommand(plugin.getConfigManager()), new Permission("lootmanager.command.reload"), "reload");
        addSubCommand(new GiveSubCommand(plugin.getLootManager()), new Permission("lootmanager.command.give"), "give");
    }

    @Override
    public String getDontPermissionMessage() {
        return configManager.getNoPermissionsMessage();
    }
}