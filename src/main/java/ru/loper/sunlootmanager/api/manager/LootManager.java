package ru.loper.sunlootmanager.api.manager;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.loper.sunlootmanager.api.modules.Loot;

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
        File lootsFolder = getLootsFolder();

        if (!lootsFolder.exists() && !lootsFolder.mkdirs()) {
            plugin.getLogger().severe("Failed to create loots directory!");
            return;
        }

        File[] files = lootsFolder.listFiles();
        if (files == null) {
            plugin.getLogger().severe("Failed to read loots directory!");
            return;
        }

        Arrays.stream(files).filter(file -> file.getName().endsWith(".yml")).forEach(file -> {
            String name = file.getName().replace(".yml", "");
            loots.put(name.toLowerCase(), new Loot(name));
        });
    }

    public boolean importLoot(String name) {
        String normalizedName = normalizeLootName(name);
        if (normalizedName == null) {
            return false;
        }

        File lootsFolder = getLootsFolder();
        File lootFile = new File(lootsFolder, normalizedName + ".yml");
        if (!lootFile.isFile() || !lootFile.getAbsoluteFile().getParentFile().equals(lootsFolder.getAbsoluteFile())) {
            return false;
        }

        Loot loot = new Loot(normalizedName);
        loots.put(loot.getName(), loot);
        return true;
    }

    public List<String> getStoredLootNames() {
        File[] files = getLootsFolder().listFiles((directory, fileName) -> fileName.endsWith(".yml"));
        if (files == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(files)
                .map(File::getName)
                .map(fileName -> fileName.substring(0, fileName.length() - 4))
                .sorted()
                .toList();
    }

    private String normalizeLootName(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }

        String normalizedName = name.trim();
        if (normalizedName.toLowerCase(Locale.ROOT).endsWith(".yml")) {
            normalizedName = normalizedName.substring(0, normalizedName.length() - 4);
        }

        if (normalizedName.isBlank() || !new File(normalizedName).getName().equals(normalizedName)) {
            return null;
        }

        return normalizedName.toLowerCase(Locale.ROOT);
    }

    private File getLootsFolder() {
        return new File(plugin.getDataFolder(), "loots");
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
        return getLoot(lootName).map(loot -> loot.generateLoot(amount)).orElse(Collections.emptyList());
    }

    public ItemStack generateLoot(String lootName) {
        return generateLoot(lootName, 1).get(0);
    }
}
