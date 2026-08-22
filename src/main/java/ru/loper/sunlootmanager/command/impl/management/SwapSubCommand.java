package ru.loper.sunlootmanager.command.impl.management;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.loper.suncore.api.command.BuildableCommand;
import ru.loper.suncore.api.command.register.SubCommandRegister;
import ru.loper.sunlootmanager.api.manager.LootManager;
import ru.loper.sunlootmanager.api.modules.Loot;
import ru.loper.sunlootmanager.config.message.Messages;

@RequiredArgsConstructor
@SubCommandRegister(permission = "lootmanager.command.swap", aliases = "swap")
public class SwapSubCommand implements BuildableCommand {
    private final LootManager lootManager;
    private final Messages messages;

    @Override
    public void handle(@NotNull CommandSender commandSender, String[] args) {
        if (args.length < 2) {
            messages.sendMessage(commandSender, messages.getUsageSwapMessage());
            return;
        }

        if (!(commandSender instanceof Player player)) {
            messages.sendMessage(commandSender, messages.getPlayerOnlyMessage());
            return;
        }

        ItemStack firstItem = player.getInventory().getItemInOffHand();
        ItemStack secondItem = player.getInventory().getItemInMainHand();

        if (isInvalidItem(firstItem)) {
            messages.sendMessage(player, messages.getSwapInvalidOffhandMessage());
            return;
        }

        if (isInvalidItem(secondItem)) {
            messages.sendMessage(player, messages.getSwapInvalidMainhandMessage());
            return;
        }

        if (firstItem.isSimilar(secondItem)) {
            messages.sendMessage(player, messages.getSwapSameItemMessage());
            return;
        }

        String targetLoot = args[1];
        int amount = performSwap(targetLoot, firstItem, secondItem, player);

        if (amount > 0) {
            sendSuccessMessage(player, amount, targetLoot);
        } else {
            messages.sendMessage(
                    player, messages.getSwapItemNotFoundMessage().replace("{item}", getItemName(firstItem)));
        }
    }

    private boolean isInvalidItem(ItemStack item) {
        return item == null || item.getType() == Material.AIR || item.getAmount() == 0;
    }

    private int performSwap(String targetLoot, ItemStack firstItem, ItemStack secondItem, Player player) {
        if (targetLoot.equalsIgnoreCase("all")) {
            return swapAllLoots(firstItem, secondItem);
        } else {
            return swapSingleLoot(targetLoot, firstItem, secondItem, player);
        }
    }

    private int swapAllLoots(ItemStack firstItem, ItemStack secondItem) {
        AtomicInteger totalAmount = new AtomicInteger(0);

        lootManager.getAllLoots().parallelStream()
                .forEach(loot -> totalAmount.addAndGet(swapLoot(loot, firstItem, secondItem)));

        return totalAmount.get();
    }

    private int swapSingleLoot(String lootName, ItemStack firstItem, ItemStack secondItem, Player player) {
        Loot loot = lootManager.getLoot(lootName).orElse(null);
        if (loot == null) {
            messages.sendMessage(player, messages.getLootNotFoundMessage());
            return 0;
        }

        return swapLoot(loot, firstItem, secondItem);
    }

    private void sendSuccessMessage(Player player, int amount, String targetLoot) {
        String message = targetLoot.equalsIgnoreCase("all")
                ? messages.getSwapSuccessAllMessage().replace("{amount}", String.valueOf(amount))
                : messages.getSwapSuccessSingleMessage()
                        .replace("{amount}", String.valueOf(amount))
                        .replace("{loot}", targetLoot);

        messages.sendMessage(player, message);
    }

    private String getItemName(ItemStack item) {
        return item.hasItemMeta() && item.getItemMeta().hasDisplayName()
                ? item.getItemMeta().getDisplayName()
                : formatMaterialName(item.getType());
    }

    private String formatMaterialName(Material material) {
        String name = material.name().toLowerCase().replace('_', ' ');
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private int swapLoot(Loot loot, ItemStack firstItem, ItemStack secondItem) {
        AtomicInteger amount = new AtomicInteger(0);

        loot.getItems().stream()
                .filter(item -> item.getItemStack().asOne().isSimilar(firstItem))
                .forEach(item -> {
                    item.setItemStack(secondItem.asOne());
                    amount.incrementAndGet();
                });

        return amount.get();
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender commandSender, String[] args) {
        if (args.length == 2) {
            List<String> completions = new ArrayList<>();
            completions.add("all");
            completions.addAll(lootManager.getLootNames());

            return completions.stream()
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        }

        return Collections.emptyList();
    }
}
