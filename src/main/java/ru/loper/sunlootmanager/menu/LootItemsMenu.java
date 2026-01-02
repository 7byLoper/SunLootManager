package ru.loper.sunlootmanager.menu;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.loper.suncore.api.config.CustomConfig;
import ru.loper.suncore.api.gui.Button;
import ru.loper.suncore.api.gui.Menu;
import ru.loper.suncore.api.items.ItemBuilder;
import ru.loper.sunlootmanager.SunLootManager;
import ru.loper.sunlootmanager.api.modules.Loot;
import ru.loper.sunlootmanager.api.modules.LootItem;
import ru.loper.sunlootmanager.config.LootConfigManager;
import ru.loper.sunlootmanager.menu.buttons.LootItemButton;

import java.util.*;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class LootItemsMenu extends Menu {
    private final List<Integer> slots;
    private final CustomConfig config;
    private final Loot loot;
    private int page;

    public LootItemsMenu(Loot loot, Menu parent, LootConfigManager configManager) {
        setParent(parent);
        this.config = configManager.getLootItemsMenuConfig();
        this.slots = config.getConfig().getIntegerList("items_slots");
        this.loot = loot;
        this.page = 0;
    }

    @Override
    public @Nullable String getTitle() {
        return config.configMessage("title").replace("{loot}", loot.getName());
    }

    @Override
    public int getSize() {
        return config.getConfig().getInt("rows", 4) * 9;
    }

    public void setParent(Menu parent) {
        if (parent == null) {
            SunLootManager plugin = SunLootManager.getInstance();
            super.setParent(new LootsMenu(plugin.getLootManager(), plugin.getConfigManager()));
            return;
        }

        super.setParent(parent);
    }

    @Override
    public void getItemsAndButtons() {
        addDecor();
        addBackButton();
        addLootItems();
        addNextPageButton();
        addBackPageButton();
    }

    private void addDecor() {
        ConfigurationSection decorSection = config.getConfig().getConfigurationSection("decor");
        if (decorSection == null) {
            return;
        }

        addDecorFromSection(decorSection);
    }

    private void addBackButton() {
        ConfigurationSection buttonSection = config.getConfig().getConfigurationSection("items.back");
        if (buttonSection == null) {
            return;
        }

        addReturnButton(buttonSection.getInt("slot"), ItemBuilder.fromConfig(buttonSection));
    }

    private void addLootItems() {
        List<LootItem> lootItems = loot.getItems().stream().toList();
        int index = page * slots.size();

        List<String> loreTemplate = config.getConfig().getStringList("loot_item_lore");

        for (int i = 0; i < slots.size() && index + i < lootItems.size(); i++) {
            LootItem lootItem = lootItems.get(index + i);
            ItemBuilder builder = new ItemBuilder(lootItem.getItemStack());
            List<String> lore = builder.lore();
            lore.addAll(generateLootItemLore(loreTemplate, lootItem));
            builder.lore(lore);

            addLootItemButton(builder, slots.get(i), lootItem);
        }
    }

    private @NotNull List<String> generateLootItemLore(List<String> loreTemplate, LootItem lootItem) {
        return loreTemplate.stream()
                .map(line -> {
                    if (line.contains("{values}")) {
                        Map<String, String> values = lootItem.getValues();

                        if (values == null || values.isEmpty()) {
                            return line.replace("{values}", "Отсутствует")
                                    .replace("{date}", "");
                        }

                        List<String> valueLines = new ArrayList<>();
                        for (Map.Entry<String, String> entry : values.entrySet()) {
                            String valueLine = line
                                    .replace("{values}", entry.getKey())
                                    .replace("{date}", entry.getValue());
                            valueLines.add(valueLine);
                        }

                        return String.join("\n", valueLines);
                    }

                    return line
                            .replace("{chance}", String.valueOf(lootItem.getChance()))
                            .replace("{min}", String.valueOf(lootItem.getMinAmount()))
                            .replace("{max}", String.valueOf(lootItem.getMaxAmount()));
                })
                .flatMap(line -> Arrays.stream(line.split("\n")))
                .collect(Collectors.toList());
    }

    private void addLootItemButton(ItemBuilder builder, int slot, LootItem lootItem) {
        buttons.add(new LootItemButton(builder.build(), slot, lootItem, loot, this));
    }

    private void addNextPageButton() {
        int maxPages = (int) Math.ceil((double) loot.getItems().size() / slots.size());
        if (page + 1 >= maxPages) {
            return;
        }

        ConfigurationSection nextPageSection = config.getConfig().getConfigurationSection("items.next_page");
        if (nextPageSection == null) {
            return;
        }

        buttons.add(new Button(ItemBuilder.fromConfig(nextPageSection).build(), nextPageSection.getInt("slot")) {
            @Override
            public void onClick(InventoryClickEvent event) {
                if (!(event.getWhoClicked() instanceof Player player)) {
                    return;
                }

                page++;
                show(player);
            }
        });
    }

    private void addBackPageButton() {
        if (page == 0) {
            return;
        }

        ConfigurationSection backPageSection = config.getConfig().getConfigurationSection("items.back_page");
        if (backPageSection == null) {
            return;
        }

        buttons.add(new Button(ItemBuilder.fromConfig(backPageSection).build(), backPageSection.getInt("slot")) {
            @Override
            public void onClick(InventoryClickEvent event) {
                if (!(event.getWhoClicked() instanceof Player player)) {
                    return;
                }

                page--;
                show(player);
            }
        });
    }

    @Override
    public void onBottomInventoryClick(@NotNull InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player && event.getClick().equals(ClickType.LEFT)) {
            ItemStack itemStack = event.getCurrentItem();
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                return;
            }

            String id = UUID.randomUUID().toString().substring(0, 8);
            LootItem lootItem = new LootItem(id, itemStack.clone().asOne(), 100, 1, itemStack.getAmount(), new HashMap<>());
            loot.addItem(id, lootItem);
            event.setCancelled(true);

            show(player);
        }
    }
}