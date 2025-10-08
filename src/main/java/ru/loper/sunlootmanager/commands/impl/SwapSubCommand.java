package ru.loper.sunlootmanager.commands.impl;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.loper.suncore.api.command.SubCommand;
import ru.loper.suncore.utils.Colorize;
import ru.loper.sunlootmanager.api.manager.LootManager;
import ru.loper.sunlootmanager.api.modules.Loot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class SwapSubCommand implements SubCommand {
    private final LootManager lootManager;

    @Override
    public void onCommand(CommandSender commandSender, String[] args) {
        if (args.length < 2) {
            commandSender.sendMessage(Colorize.parse("&#FF5555▶ &fИспользование: &7/loot swap <название_лута|all>"));
            return;
        }

        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(Colorize.parse("&#FF5555▶ &fДанная команда доступна только игрокам!"));
            return;
        }

        ItemStack firstItem = player.getInventory().getItemInOffHand();
        ItemStack secondItem = player.getInventory().getItemInMainHand();

        if (isInvalidItem(firstItem)) {
            player.sendMessage(Colorize.parse("&#FF5555▶ &fДля замены предмета возьмите его во вторую руку!"));
            return;
        }

        if (isInvalidItem(secondItem)) {
            player.sendMessage(Colorize.parse("&#FF5555▶ &fВы не можете заменить предмет на воздух!"));
            return;
        }

        if (firstItem.isSimilar(secondItem)) {
            player.sendMessage(Colorize.parse("&#FF5555▶ &fПредметы для замены не должны быть одинаковыми!"));
            return;
        }

        String targetLoot = args[1];
        int amount = performSwap(targetLoot, firstItem, secondItem, player);

        if (amount > 0) {
            sendSuccessMessage(player, amount, targetLoot);
        } else {
            player.sendMessage(Colorize.parse("&#FFAA00▶ &fПредмет &7%s &fне был найден в указанном луте.".formatted(getItemName(firstItem))));
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
            player.sendMessage(Colorize.parse("&#FF5555▶ &fЛут с названием &7%s &fне существует!".formatted(lootName)));
            return 0;
        }

        return swapLoot(loot, firstItem, secondItem);
    }

    private void sendSuccessMessage(Player player, int amount, String targetLoot) {
        String message = targetLoot.equalsIgnoreCase("all") ?
                "&#55FF55▶ &fУспешно заменено &a%d &fпредметов во &aвсех &fлутах".formatted(amount) :
                "&#55FF55▶ &fУспешно заменено &a%d &fпредметов в луте &a%s".formatted(amount, targetLoot);

        player.sendMessage(Colorize.parse(message));
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
    public List<String> onTabCompleter(CommandSender commandSender, String[] args) {
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