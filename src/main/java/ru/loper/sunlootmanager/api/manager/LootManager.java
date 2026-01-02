package ru.loper.sunlootmanager.api.manager;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.loper.sunlootmanager.api.modules.Loot;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LootManager {
    private final Map<String, Loot> loots;
    private final Plugin plugin;

    public LootManager(Plugin plugin) {
        this.plugin = plugin;
        this.loots = new ConcurrentHashMap<>();
        reload();
    }

    public void reload() {
        loots.clear();
        File lootsFolder = new File(plugin.getDataFolder(), "loots");

        if (!lootsFolder.exists() && !lootsFolder.mkdirs()) {
            plugin.getLogger().severe("Failed to create loots directory!");
            return;
        }

        Arrays.stream(Objects.requireNonNull(lootsFolder.listFiles()))
                .filter(file -> file.getName().endsWith(".yml"))
                .forEach(file -> {
                    String name = file.getName().replace(".yml", "");
                    loots.put(name.toLowerCase(), new Loot(name));
                });
    }

    public void addLoot(Loot loot) {
        loots.put(loot.getName(), loot);
    }

    public boolean createLoot(String name) {
        if (loots.containsKey(name.toLowerCase())) {
            return false;
        }
        Loot loot = new Loot(name);
        loots.put(name.toLowerCase(), loot);
        return true;
    }

    public boolean deleteLoot(String name) {
        Loot removed = loots.remove(name.toLowerCase());
        if (removed != null) {
            File file = new File(plugin.getDataFolder(), "loots/" + name + ".yml");
            return file.delete();
        }

        return false;
    }

    public Optional<Loot> getLoot(String name) {
        return Optional.ofNullable(loots.get(name.toLowerCase()));
    }

    public Set<String> getLootNames() {
        return Collections.unmodifiableSet(loots.keySet());
    }

    public void saveAll() {
        loots.values().forEach(Loot::save);
    }

    public Collection<Loot> getAllLoots() {
        return loots.values();
    }

    public List<ItemStack> generateLoot(String lootName, int amount) {
        return getLoot(lootName)
                .map(loot -> loot.generateLoot(amount))
                .orElse(Collections.emptyList());
    }

    public ItemStack generateLoot(String lootName) {
        return generateLoot(lootName, 1).getFirst();
    }
}