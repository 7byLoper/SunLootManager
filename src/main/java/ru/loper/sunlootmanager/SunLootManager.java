package ru.loper.sunlootmanager;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.loper.sunlootmanager.api.manager.LootManager;
import ru.loper.sunlootmanager.commands.LootCommand;
import ru.loper.sunlootmanager.config.LootConfigManager;
import ru.loper.sunlootmanager.listeners.PlayerChatListener;

import java.util.Optional;

@Getter
public final class SunLootManager extends JavaPlugin {
    @Getter
    private static SunLootManager instance;

    private LootConfigManager configManager;
    private LootManager lootManager;

    @Override
    public void onEnable() {
        instance = this;
        configManager = new LootConfigManager(this);
        lootManager = new LootManager(this);

        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(), this);

        Optional.ofNullable(getCommand("lootmanager"))
                .orElseThrow(() -> new IllegalStateException("Command 'lootmanager' not found!"))
                .setExecutor(new LootCommand(this));
    }

    @Override
    public void onDisable() {
        lootManager.saveAll();
    }
}
