package ru.loper.sunlootmanager.api.modules;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import ru.loper.suncore.api.config.CustomConfig;
import ru.loper.sunlootmanager.SunLootManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Loot {
    private final Map<String, LootItem> lootItems;
    private final CustomConfig config;
    @Getter
    private final String name;

    public Loot(String name) {
        this.name = name.toLowerCase();
        this.lootItems = new ConcurrentHashMap<>();
        this.config = new CustomConfig("loots/" + this.name, true, SunLootManager.getInstance());

        loadFromConfig();
    }

    public void addItem(String id, LootItem item) {
        lootItems.put(id.toLowerCase(), item);
    }

    public void removeItem(String id) {
        lootItems.remove(id.toLowerCase());
    }

    public Collection<LootItem> getItems() {
        return Collections.unmodifiableCollection(lootItems.values());
    }

    public List<LootItem> generateLootItems(int amount) {
        List<LootItem> result = new ArrayList<>();

        if (lootItems.isEmpty() || amount <= 0) {
            return result;
        }

        List<LootItem> items = new ArrayList<>(lootItems.values());
        Collections.shuffle(items);

        for (int i = 0; i < amount; i++) {
            for (LootItem item : items) {
                if (item.shouldDrop()) {
                    result.add(item);
                }
            }
        }

        if (result.size() > amount) {
            return result.subList(0, amount);
        }

        return result;
    }

    public List<ItemStack> generateLoot(int amount) {
        return generateLootItems(amount)
                .stream()
                .map(LootItem::getItemStackRandomAmount)
                .toList();
    }

    public ItemStack generateLoot() {
        return generateLoot(1).get(0);
    }

    public LootItem generateLootItem() {
        return generateLootItems(1).get(0);
    }


    public void save() {
        ConfigurationSection itemsSection = config.getConfig().createSection("items");
        lootItems.forEach((id, item) -> item.saveToSection(itemsSection.createSection(id)));
        config.saveConfig();
    }

    private void loadFromConfig() {
        ConfigurationSection itemsSection = config.getConfig().getConfigurationSection("items");
        if (itemsSection != null) {
            itemsSection.getKeys(false).forEach(key -> {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                if (itemSection != null) {
                    lootItems.put(key.toLowerCase(), new LootItem(itemSection));
                }
            });
        }
    }
}