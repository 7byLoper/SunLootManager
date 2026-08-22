package ru.loper.sunlootmanager.config;

import lombok.Getter;
import org.bukkit.plugin.Plugin;
import ru.loper.suncore.api.config.ConfigManager;
import ru.loper.suncore.api.config.CustomConfig;
import ru.loper.sunlootmanager.config.message.Messages;

@Getter
public class LootConfigManager extends ConfigManager {
    private Messages messages;

    public LootConfigManager(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void loadConfigs() {
        addCustomConfig(new CustomConfig("messages.yml", plugin));
        addCustomConfig(new CustomConfig("menu/loots_menu.yml", plugin));
        addCustomConfig(new CustomConfig("menu/loot_items_menu.yml", plugin));
    }

    @Override
    public void loadValues() {
        messages = new Messages(getCustomConfig("messages.yml").getConfig());
    }

    public CustomConfig getLootsMenuConfig() {
        return getCustomConfig("menu/loots_menu.yml");
    }

    public CustomConfig getLootItemsMenuConfig() {
        return getCustomConfig("menu/loot_items_menu.yml");
    }
}
