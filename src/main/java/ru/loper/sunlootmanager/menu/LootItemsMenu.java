package ru.loper.sunlootmanager.menu;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
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

import java.util.List;
import java.util.UUID;

public class LootItemsMenu extends Menu {
    private final List<Integer> slots;
    private final CustomConfig config;
    private final Loot loot;
    private boolean back;
    private int page;

    public LootItemsMenu(Loot loot, Menu parent, LootConfigManager configManager) {
        setParent(parent);
        config = configManager.getLootItemsMenuConfig();
        slots = config.getConfig().getIntegerList("items_slots");
        this.loot = loot;
        back = true;
        page = 0;
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
        if (decorSection == null) return;
        addDecorFromSection(decorSection);
    }

    private void addBackButton() {
        ConfigurationSection buttonSection = config.getConfig().getConfigurationSection("items.back");
        if (buttonSection == null) return;
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
            lore.addAll(loreTemplate.stream()
                    .map(line -> line
                            .replace("{chance}", String.valueOf(lootItem.getChance()))
                            .replace("{min}", String.valueOf(lootItem.getMinAmount()))
                            .replace("{max}", String.valueOf(lootItem.getMaxAmount()))
                    ).toList());
            builder.lore(lore);

            addLootItemButton(builder, slots.get(i), lootItem);
        }
    }

    private void addLootItemButton(ItemBuilder builder, int slot, LootItem lootItem) {
        buttons.add(new Button(builder.build(), slot) {
            @Override
            public void onClick(InventoryClickEvent e) {
                if (!(e.getWhoClicked() instanceof Player player)) return;

                boolean update = true;

                switch (e.getClick()) {
                    case LEFT -> lootItem.addChance(1);
                    case RIGHT -> lootItem.takeChance(1);
                    case SHIFT_LEFT -> lootItem.addChance(10);
                    case SHIFT_RIGHT -> lootItem.takeChance(10);
                    case DROP -> loot.removeItem(lootItem.getId());
                    case SWAP_OFFHAND -> player.getInventory().addItem(lootItem.getItemStack()).values().forEach(
                            item -> player.getWorld().dropItemNaturally(player.getLocation(), item)
                    );
                    case NUMBER_KEY -> {
                        int key = e.getHotbarButton();
                        switch (key) {
                            case 0 ->
                                    lootItem.setAmountRange(lootItem.getMinAmount(), Math.max(lootItem.getMinAmount(), lootItem.getMaxAmount() - 1));
                            case 1 -> lootItem.setAmountRange(lootItem.getMinAmount(), lootItem.getMaxAmount() + 1);
                            case 2 ->
                                    lootItem.setAmountRange(Math.max(1, lootItem.getMinAmount() - 1), lootItem.getMaxAmount());
                            case 3 -> lootItem.setAmountRange(lootItem.getMinAmount() + 1, lootItem.getMaxAmount());
                            default -> update = false;
                        }
                    }
                    default -> update = false;
                }

                if (update) update(player);
            }
        });
    }

    private void addNextPageButton() {
        int maxPages = (int) Math.ceil((double) loot.getItems().size() / slots.size());
        if (page + 1 >= maxPages) return;

        ConfigurationSection nextPageSection = config.getConfig().getConfigurationSection("items.next_page");
        if (nextPageSection == null) return;

        buttons.add(new Button(ItemBuilder.fromConfig(nextPageSection).build(), nextPageSection.getInt("slot")) {
            @Override
            public void onClick(InventoryClickEvent e) {
                if (e.getWhoClicked() instanceof Player player) {
                    page++;
                    update(player);
                }
            }
        });
    }

    private void addBackPageButton() {
        if (page == 0) return;

        ConfigurationSection backPageSection = config.getConfig().getConfigurationSection("items.back_page");
        if (backPageSection == null) return;

        buttons.add(new Button(ItemBuilder.fromConfig(backPageSection).build(), backPageSection.getInt("slot")) {
            @Override
            public void onClick(InventoryClickEvent e) {
                if (e.getWhoClicked() instanceof Player player) {
                    page--;
                    update(player);
                }
            }
        });
    }

    @Override
    public void onBottomInventoryClick(@NotNull InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player && event.getClick().equals(ClickType.LEFT)) {
            ItemStack itemStack = event.getCurrentItem();
            if (itemStack == null || itemStack.getType() == Material.AIR) return;

            String id = UUID.randomUUID().toString().substring(0, 8);
            LootItem lootItem = new LootItem(id, itemStack.clone().asOne(), 100, 1, itemStack.getAmount());
            loot.addItem(id, lootItem);
            event.setCancelled(true);

            update(player);
        }
    }

    public void update(@NotNull Player player) {
        super.show(player);
        back = false;
    }

    @Override
    public void onClose(@NotNull InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        if (getParent() == null) return;
        if (!back) {
            back = true;
            return;
        }
        getParent().show(player);
    }
}