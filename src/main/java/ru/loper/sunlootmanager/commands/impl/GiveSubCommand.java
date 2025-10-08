package ru.loper.sunlootmanager.commands.impl;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.loper.suncore.api.command.SubCommand;
import ru.loper.suncore.utils.Colorize;
import ru.loper.sunlootmanager.api.manager.LootManager;
import ru.loper.sunlootmanager.api.modules.Loot;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class GiveSubCommand implements SubCommand {
    private final LootManager lootManager;

    @Override
    public void onCommand(CommandSender commandSender, String[] args) {
        if (args.length < 4) {
            commandSender.sendMessage(Colorize.parse("&#FF5555▶ &7Использование: &f/loot give <игрок> <название_лута> <кол-во>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            commandSender.sendMessage(Colorize.parse("&#FF5555▶ &fИгрок &7" + args[1] + " &fне найден или offline!"));
            return;
        }

        Optional<Loot> optionalLoot = lootManager.getLoot(args[2]);
        if (optionalLoot.isEmpty()) {
            commandSender.sendMessage(Colorize.parse("&#FF5555▶ &fЛут с таким названием &7отсутствует&f!"));
            return;
        }

        try {
            int amount = Integer.parseInt(args[3]);
            Loot loot = optionalLoot.get();

            loot.generateLoot(amount).forEach(item -> target.getInventory().addItem(item));

            commandSender.sendMessage(Colorize.parse("&#55FF55▶ &fЛут &#55FF55" + loot.getName() + " &fвыдан игроку &7" + target.getName()));
            target.sendMessage(Colorize.parse("&#55FF55▶ &fВы получили лут &#55FF55" + loot.getName()));
        } catch (NumberFormatException e) {
            commandSender.sendMessage(Colorize.parse("&#FF5555▶ &fНеверный формат числа '%s'".formatted(args[3])));
        }
    }

    @Override
    public List<String> onTabCompleter(CommandSender commandSender, String[] args) {
        if (args.length == 2) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        } else if (args.length == 3) {
            return lootManager.getLootNames().stream()
                    .filter(line -> line.toLowerCase().startsWith(args[2].toLowerCase()))
                    .toList();
        } else if (args.length == 4) {
            return Stream.of("1", "8", "32", "64")
                    .filter(line -> line.toLowerCase().startsWith(args[3].toLowerCase()))
                    .toList();
        }
        return Collections.emptyList();
    }
}