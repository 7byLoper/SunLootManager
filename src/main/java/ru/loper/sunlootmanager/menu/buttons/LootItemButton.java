package ru.loper.sunlootmanager.menu.buttons;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.loper.suncore.api.gui.Button;
import ru.loper.sunlootmanager.SunLootManager;
import ru.loper.sunlootmanager.api.modules.Loot;
import ru.loper.sunlootmanager.api.modules.LootItem;
import ru.loper.sunlootmanager.menu.LootItemsMenu;

import java.util.HashMap;
import java.util.Map;

public class LootItemButton extends Button {
    @Getter
    private static final Map<String, PlayerValuesParts> SET_VALUES_PLAYERS = new HashMap<>();

    private final LootItem lootItem;
    private final Loot loot;

    private final LootItemsMenu menu;

    public LootItemButton(ItemStack itemStack, int slot, LootItem lootItem, Loot loot, LootItemsMenu menu) {
        super(itemStack, slot);
        this.lootItem = lootItem;
        this.loot = loot;
        this.menu = menu;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        boolean update = true;

        switch (event.getClick()) {
            case LEFT -> lootItem.addChance(1);
            case RIGHT -> lootItem.takeChance(1);
            case SHIFT_LEFT -> lootItem.addChance(10);
            case SHIFT_RIGHT -> lootItem.takeChance(10);
            case DROP -> loot.removeItem(lootItem.getId());
            case SWAP_OFFHAND -> player.getInventory().addItem(lootItem.getItemStack()).values().forEach(item ->
                    player.getWorld().dropItemNaturally(player.getLocation(), item));
            case NUMBER_KEY -> update = handleNumberKey(event, player, update);
            default -> update = false;
        }

        if (update) {
            menu.show(player);
        }
    }

    private boolean handleNumberKey(InventoryClickEvent event, Player player, boolean update) {
        switch (event.getHotbarButton()) {
            case 0 ->
                    lootItem.setAmountRange(lootItem.getMinAmount(), Math.max(lootItem.getMinAmount(), lootItem.getMaxAmount() - 1));
            case 1 -> lootItem.setAmountRange(lootItem.getMinAmount(), lootItem.getMaxAmount() + 1);
            case 2 -> lootItem.setAmountRange(Math.max(1, lootItem.getMinAmount() - 1), lootItem.getMaxAmount());
            case 3 -> lootItem.setAmountRange(lootItem.getMinAmount() + 1, lootItem.getMaxAmount());
            case 4 -> {
                player.closeInventory();
                SET_VALUES_PLAYERS.put(player.getName(), new PlayerValuesParts(lootItem, menu));

                Bukkit.getScheduler().runTaskLater(SunLootManager.getInstance(), () -> SET_VALUES_PLAYERS.remove(player.getName()), 200L);
                update = false;
            }
            default -> update = false;
        }

        return update;
    }

    public record PlayerValuesParts(LootItem lootItem, LootItemsMenu lootItemsMenu) {
    }
}
