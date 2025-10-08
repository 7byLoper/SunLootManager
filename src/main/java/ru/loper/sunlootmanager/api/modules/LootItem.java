package ru.loper.sunlootmanager.api.modules;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public class LootItem {
    private final String id;
    private final ItemStack itemStack;
    private int chance; // 0-100%
    private int minAmount;
    private int maxAmount;

    public LootItem(String id, ItemStack itemStack, int chance, int minAmount, int maxAmount) {
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
    }

    public LootItem(@NotNull ConfigurationSection section) {
        this(
                section.getName(),
                section.getItemStack("item", new ItemStack(Material.AIR)),
                section.getInt("chance", 100),
                section.getInt("minAmount", 1),
                section.getInt("maxAmount", 1)
        );
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