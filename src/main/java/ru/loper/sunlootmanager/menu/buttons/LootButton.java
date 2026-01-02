package ru.loper.sunlootmanager.menu.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.loper.suncore.api.gui.Button;
import ru.loper.sunlootmanager.api.modules.Loot;
import ru.loper.sunlootmanager.menu.LootItemsMenu;
import ru.loper.sunlootmanager.menu.LootsMenu;

public class LootButton extends Button {
    private final Loot loot;
    private final LootsMenu lootsMenu;

    public LootButton(ItemStack itemStack, int slot, Loot loot, LootsMenu lootsMenu) {
        super(itemStack, slot);

        this.loot = loot;
        this.lootsMenu = lootsMenu;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        switch (event.getClick()) {
            case LEFT -> new LootItemsMenu(loot, lootsMenu, lootsMenu.getConfigManager()).show(player);
            case DROP -> {
                lootsMenu.getLootManager().deleteLoot(loot.getName());
                lootsMenu.show(player);
            }
        }

    }
}
