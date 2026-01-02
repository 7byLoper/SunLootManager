package ru.loper.sunlootmanager.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.loper.sunlootmanager.menu.buttons.LootItemButton;

public class PlayerChatListener implements Listener {
    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        if (!LootItemButton.getSET_VALUES_PLAYERS().containsKey(player.getName())) {
            return;
        }

        String message = LegacyComponentSerializer.legacyAmpersand().serialize(event.message());
        String[] parts = message.split(":", 2);
        if (parts.length < 2) {
            return;
        }

        var valuesParts = LootItemButton.getSET_VALUES_PLAYERS().get(player.getName());
        valuesParts.lootItem().setValue(parts[0], parts[1]);
        valuesParts.lootItemsMenu().show(player);
        event.setCancelled(true);
    }
}
