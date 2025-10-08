package ru.loper.sunlootmanager.menu;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.loper.suncore.api.config.CustomConfig;
import ru.loper.suncore.api.gui.Button;
import ru.loper.suncore.api.gui.Menu;
import ru.loper.suncore.api.items.ItemBuilder;
import ru.loper.sunlootmanager.api.manager.LootManager;
import ru.loper.sunlootmanager.api.modules.Loot;
import ru.loper.sunlootmanager.config.LootConfigManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LootsMenu extends Menu {
    protected final LootConfigManager configManager;
    protected final LootManager lootManager;
    protected final CustomConfig config;
    protected boolean back;
    protected int page;

    public LootsMenu(LootManager lootManager, LootConfigManager configManager) {
        this.configManager = configManager;
        this.lootManager = lootManager;
        this.config = configManager.getLootsMenuConfig();
        this.back = true;
        this.page = 0;
    }

    @Override
    public @Nullable String getTitle() {
        return config.configMessage("title");
    }

    @Override
    public int getSize() {
        return config.getConfig().getInt("rows", 4) * 9;
    }

    @Override
    public void getItemsAndButtons() {
        addDecor();
        addLootButtons();
        addCreateLootButton();
        addNextPageButton();
        addBackPageButton();
    }

    private void addDecor() {
        ConfigurationSection decorSection = config.getConfig().getConfigurationSection("decor");
        if (decorSection == null) return;
        addDecorFromSection(decorSection);
    }

    private void addLootButtons() {
        List<Integer> slots = config.getConfig().getIntegerList("loots-slots");
        List<Loot> loots = new ArrayList<>(lootManager.getAllLoots());
        int start = page * slots.size();

        ConfigurationSection lootFormSection = getLootFormSection();
        if (lootFormSection == null) return;

        for (int i = 0; i < slots.size() && (start + i) < loots.size(); i++) {
            Loot loot = loots.get(start + i);

            ItemBuilder builder = ItemBuilder.fromConfig(lootFormSection);
            builder.name(builder.name().replace("{loot}", loot.getName()));
            builder.lore(builder.lore().stream()
                    .map(line -> line.replace("{loot}", loot.getName())
                            .replace("{items}", String.valueOf(loot.getItems().size())))
                    .toList());

            buttons.add(new Button(builder.build(), slots.get(i)) {
                @Override
                public void onClick(InventoryClickEvent event) {
                    onLootClick(event, loot);
                }
            });
        }
    }

    public ConfigurationSection getLootFormSection() {
        return config.getConfig().getConfigurationSection("loot_form");
    }

    public void onLootClick(InventoryClickEvent event, Loot loot) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        switch (event.getClick()) {
            case LEFT -> new LootItemsMenu(loot, getInstance(), configManager).show(player);
            case DROP -> {
                lootManager.deleteLoot(loot.getName());
                update(player);
            }
        }
    }

    private void addCreateLootButton() {
        ConfigurationSection section = config.getConfig().getConfigurationSection("items.create_loot");
        if (section == null) return;
        buttons.add(new Button(ItemBuilder.fromConfig(section).build(), section.getInt("slot")) {
            @Override
            public void onClick(InventoryClickEvent e) {
                if (e.getWhoClicked() instanceof Player player) {
                    String name = "loot-" + UUID.randomUUID().toString().substring(0, 6);
                    Loot newLoot = new Loot(name);
                    lootManager.addLoot(newLoot);
                    new LootItemsMenu(newLoot, getInstance(), configManager).show(player);
                }
            }
        });
    }

    private void addNextPageButton() {
        int total = lootManager.getAllLoots().size();
        int maxPages = (int) Math.ceil((double) total / config.getConfig().getIntegerList("loots-slots").size());
        if (page + 1 >= maxPages) return;

        ConfigurationSection section = config.getConfig().getConfigurationSection("items.next_page");
        if (section == null) return;

        buttons.add(new Button(ItemBuilder.fromConfig(section).build(), section.getInt("slot")) {
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
        if (page <= 0) return;

        ConfigurationSection section = config.getConfig().getConfigurationSection("items.back_page");
        if (section == null) return;

        buttons.add(new Button(ItemBuilder.fromConfig(section).build(), section.getInt("slot")) {
            @Override
            public void onClick(InventoryClickEvent e) {
                if (e.getWhoClicked() instanceof Player player) {
                    page--;
                    update(player);
                }
            }
        });
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
