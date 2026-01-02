package ru.loper.sunlootmanager.api.modules;

import lombok.Data;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Data
public class LootItem {
    private final String id;
    private ItemStack itemStack;
    private int chance; // 0-100%
    private int minAmount;
    private int maxAmount;

    private Map<String, String> values;

    public LootItem(String id, ItemStack itemStack, int chance, int minAmount, int maxAmount, Map<String, String> values) {
        if (itemStack == null) {
            throw new IllegalArgumentException("ItemStack cannot be null");
        }
        if (chance < 0 || chance > 100) {
            throw new IllegalArgumentException("Chance must be between 0 and 100");
        }
        if (minAmount < 1 || maxAmount < minAmount) {
            throw new IllegalArgumentException("Invalid amount range");
        }

        this.id = id;
        this.itemStack = itemStack.clone();
        this.itemStack.setAmount(1);
        this.chance = chance;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.values = values;
    }

    public LootItem(@NotNull ConfigurationSection section) {
        this(
                section.getName(),
                section.getItemStack("item", new ItemStack(Material.AIR)),
                section.getInt("chance", 100),
                section.getInt("minAmount", 1),
                section.getInt("maxAmount", 1),
                getMapValues(section)
        );
    }

    private static Map<String, String> getMapValues(@NotNull ConfigurationSection section) {
        ConfigurationSection valuesSection = section.getConfigurationSection("values");
        Map<String, String> values = new HashMap<>();

        if (valuesSection == null) {
            return values;
        }

        for (String key : valuesSection.getKeys(false)) {
            values.put(key, valuesSection.getString(key));
        }

        return values;
    }

    public boolean shouldDrop() {
        return ThreadLocalRandom.current().nextInt(100) < chance;
    }

    public int getRandomAmount() {
        return minAmount == maxAmount ? maxAmount :
                ThreadLocalRandom.current().nextInt(minAmount, maxAmount + 1);
    }

    public ItemStack getItemStack() {
        return itemStack.clone();
    }

    public ItemStack getItemStackRandomAmount() {
        ItemStack result = itemStack.clone();
        result.setAmount(getRandomAmount());
        return result;
    }

    public void saveToSection(ConfigurationSection section) {
        section.set("item", itemStack);
        section.set("chance", chance);
        section.set("minAmount", minAmount);
        section.set("maxAmount", maxAmount);
        section.set("values", values);
    }

    public void setValue(String key, String value) {
        values.put(key, value);
    }

    public String getValue(String key) {
        return values.get(key);
    }

    public boolean hasValue(String key) {
        return values.containsKey(key);
    }

    public void addChance(int add) {
        chance = Math.min(100, chance + add);
    }

    public void takeChance(int add) {
        chance = Math.max(0, chance - add);
    }

    public void setMinAmount(int minAmount) {
        if (minAmount < 1 || minAmount > maxAmount) {
            return;
        }
        this.minAmount = minAmount;
    }

    public void setMaxAmount(int maxAmount) {
        if (maxAmount < minAmount) {
            return;
        }
        this.maxAmount = maxAmount;
    }

    public void setAmountRange(int minAmount, int maxAmount) {
        if (minAmount < 1 || maxAmount < minAmount) {
            return;
        }
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }
}