package ru.loper.sunlootmanager.config;

import lombok.Getter;
import org.bukkit.plugin.Plugin;
import ru.loper.suncore.api.colorize.StringColorize;
import ru.loper.suncore.api.config.ConfigManager;
import ru.loper.suncore.api.config.CustomConfig;

@Getter
public class LootConfigManager extends ConfigManager {
    private String noPermissionsMessage;
    private String usageCreateMessage;
    private String usageEditMessage;
    private String usageGiveMessage;
    private String usageInfoMessage;
    private String usageRemoveMessage;
    private String usageSwapMessage;
    private String lootAlreadyExistsMessage;
    private String lootNotFoundMessage;
    private String lootNotFoundGiveMessage;
    private String lootCreatedMessage;
    private String lootDeletedMessage;
    private String playerOnlyMessage;
    private String playerNotFoundMessage;
    private String editingLootMessage;
    private String invalidNumberFormatMessage;
    private String giveSuccessMessage;
    private String giveReceivedMessage;
    private String lootInfoHeaderMessage;
    private String lootInfoNameMessage;
    private String lootInfoItemsMessage;
    private String lootListEmptyMessage;
    private String lootListHeaderMessage;
    private String lootListEntryMessage;
    private String configReloadedMessage;
    private String openingLootsMenuMessage;
    private String swapInvalidOffhandMessage;
    private String swapInvalidMainhandMessage;
    private String swapSameItemMessage;
    private String swapItemNotFoundMessage;
    private String swapSuccessAllMessage;
    private String swapSuccessSingleMessage;

    public LootConfigManager(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void loadConfigs() {
        plugin.saveDefaultConfig();
        addCustomConfig(new CustomConfig("menu/loots_menu.yml", plugin));
        addCustomConfig(new CustomConfig("menu/loot_items_menu.yml", plugin));
    }

    @Override
    public void loadValues() {
        noPermissionsMessage = StringColorize.parse(plugin.getConfig().getString("messages.no_permissions", ""));
        usageCreateMessage = StringColorize.parse(plugin.getConfig().getString("messages.usage.create", "&#FF5555▶ &7Использование: &f/loot create <название>"));
        usageEditMessage = StringColorize.parse(plugin.getConfig().getString("messages.usage.edit", "&#FF5555▶ &7Использование: &f/loot edit <название>"));
        usageGiveMessage = StringColorize.parse(plugin.getConfig().getString("messages.usage.give", "&#FF5555▶ &7Использование: &f/loot give <игрок> <название_лута> <кол-во>"));
        usageInfoMessage = StringColorize.parse(plugin.getConfig().getString("messages.usage.info", "&#FF5555▶ &7Использование: &f/loot info <название>"));
        usageRemoveMessage = StringColorize.parse(plugin.getConfig().getString("messages.usage.remove", "&#FF5555▶ &7Использование: &f/loot remove <название>"));
        usageSwapMessage = StringColorize.parse(plugin.getConfig().getString("messages.usage.swap", "&#FF5555▶ &fИспользование: &7/loot swap <название_лута|all>"));
        lootAlreadyExistsMessage = StringColorize.parse(plugin.getConfig().getString("messages.loot_already_exists", "&#FF5555▶ &fЛут с таким названием &7уже существует&f!"));
        lootNotFoundMessage = StringColorize.parse(plugin.getConfig().getString("messages.loot_not_found", "&#FF5555▶ &fЛут с таким названием &7отсутствует&f!"));
        lootNotFoundGiveMessage = StringColorize.parse(plugin.getConfig().getString("messages.loot_not_found_give", "&#FF5555▶ &fЛут с таким названием &7отсутствует&f!"));
        lootCreatedMessage = StringColorize.parse(plugin.getConfig().getString("messages.loot_created", "&#55FF55▶ &fЛут &#55FF55{name} &fуспешно создан!"));
        lootDeletedMessage = StringColorize.parse(plugin.getConfig().getString("messages.loot_deleted", "&#55FF55▶ &fЛут &#55FF55{name} &fуспешно удален!"));
        playerOnlyMessage = StringColorize.parse(plugin.getConfig().getString("messages.player_only", "&#FF5555▶ &fДанная команда доступна только &7игрокам&f!"));
        playerNotFoundMessage = StringColorize.parse(plugin.getConfig().getString("messages.player_not_found", "&#FF5555▶ &fИгрок &7{player} &fне найден или offline!"));
        editingLootMessage = StringColorize.parse(plugin.getConfig().getString("messages.editing_loot", "&#55FF55▶ &fОткрытие редактора лута &#55FF55{name}"));
        invalidNumberFormatMessage = StringColorize.parse(plugin.getConfig().getString("messages.invalid_number_format", "&#FF5555▶ &fНеверный формат числа '{number}'"));
        giveSuccessMessage = StringColorize.parse(plugin.getConfig().getString("messages.give_success", "&#55FF55▶ &fЛут &#55FF55{loot} &fвыдан игроку &7{player}"));
        giveReceivedMessage = StringColorize.parse(plugin.getConfig().getString("messages.give_received", "&#55FF55▶ &fВы получили лут &#55FF55{loot}"));
        lootInfoHeaderMessage = StringColorize.parse(plugin.getConfig().getString("messages.loot_info_header", "&#FFAA00▶ &6Информация о луте:"));
        lootInfoNameMessage = StringColorize.parse(plugin.getConfig().getString("messages.loot_info_name", "&#55FFFF▪ &fНазвание: &7{name}"));
        lootInfoItemsMessage = StringColorize.parse(plugin.getConfig().getString("messages.loot_info_items", "&#55FFFF▪ &fПредметов: &7{count}"));
        lootListEmptyMessage = StringColorize.parse(plugin.getConfig().getString("messages.loot_list_empty", "&#FFAA00▶ &fСозданные луты &7отсутствуют&f!"));
        lootListHeaderMessage = StringColorize.parse(plugin.getConfig().getString("messages.loot_list_header", "&#55FF55▶ &aСписок лутов &7({count}):"));
        lootListEntryMessage = StringColorize.parse(plugin.getConfig().getString("messages.loot_list_entry", "&#55FFFF{number}. &7{name}"));
        configReloadedMessage = StringColorize.parse(plugin.getConfig().getString("messages.config_reloaded", "&#55FF55▶ &fКонфигурация плагина успешно &7перезагружена&f!"));
        openingLootsMenuMessage = StringColorize.parse(plugin.getConfig().getString("messages.opening_loots_menu", "&#55FF55▶ &fОткрытие меню лутов..."));
        swapInvalidOffhandMessage = StringColorize.parse(plugin.getConfig().getString("messages.swap_invalid_offhand", "&#FF5555▶ &fДля замены предмета возьмите его во вторую руку!"));
        swapInvalidMainhandMessage = StringColorize.parse(plugin.getConfig().getString("messages.swap_invalid_mainhand", "&#FF5555▶ &fВы не можете заменить предмет на воздух!"));
        swapSameItemMessage = StringColorize.parse(plugin.getConfig().getString("messages.swap_same_item", "&#FF5555▶ &fПредметы для замены не должны быть одинаковыми!"));
        swapItemNotFoundMessage = StringColorize.parse(plugin.getConfig().getString("messages.swap_item_not_found", "&#FFAA00▶ &fПредмет &7{item} &fне был найден в указанном луте."));
        swapSuccessAllMessage = StringColorize.parse(plugin.getConfig().getString("messages.swap_success_all", "&#55FF55▶ &fУспешно заменено &a{amount} &fпредметов во &aвсех &fлутах"));
        swapSuccessSingleMessage = StringColorize.parse(plugin.getConfig().getString("messages.swap_success_single", "&#55FF55▶ &fУспешно заменено &a{amount} &fпредметов в луте &a{loot}"));
    }

    public CustomConfig getLootsMenuConfig() {
        return getCustomConfig("menu/loots_menu.yml");
    }

    public CustomConfig getLootItemsMenuConfig() {
        return getCustomConfig("menu/loot_items_menu.yml");
    }
}