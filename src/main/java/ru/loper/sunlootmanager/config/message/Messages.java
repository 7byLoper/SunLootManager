package ru.loper.sunlootmanager.config.message;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import ru.loper.suncore.api.colorize.StringColorize;

@Getter
public class Messages {
    private final FileConfiguration config;

    private final String noPermissionsMessage;
    private final String usageCreateMessage;
    private final String usageEditMessage;
    private final String usageGiveMessage;
    private final String usageImportMessage;
    private final String usageInfoMessage;
    private final String usageRemoveMessage;
    private final String usageStorageMessage;
    private final String usageSwapMessage;
    private final String lootAlreadyExistsMessage;
    private final String lootNotFoundMessage;
    private final String lootNotFoundGiveMessage;
    private final String lootCreatedMessage;
    private final String lootDeletedMessage;
    private final String lootImportedMessage;
    private final String playerOnlyMessage;
    private final String playerNotFoundMessage;
    private final String editingLootMessage;
    private final String invalidNumberFormatMessage;
    private final String giveSuccessMessage;
    private final String giveReceivedMessage;
    private final String lootInfoHeaderMessage;
    private final String lootInfoNameMessage;
    private final String lootInfoItemsMessage;
    private final String lootListEmptyMessage;
    private final String lootListHeaderMessage;
    private final String lootListEntryMessage;
    private final String openingLootsMenuMessage;
    private final String storageSavedMessage;
    private final String storageReloadedMessage;
    private final String swapInvalidOffhandMessage;
    private final String swapInvalidMainhandMessage;
    private final String swapSameItemMessage;
    private final String swapItemNotFoundMessage;
    private final String swapSuccessAllMessage;
    private final String swapSuccessSingleMessage;

    public Messages(FileConfiguration config) {
        this.config = config;
        noPermissionsMessage = loadMessage("no_permissions");
        usageCreateMessage = loadMessage("usage.create");
        usageEditMessage = loadMessage("usage.edit");
        usageGiveMessage = loadMessage("usage.give");
        usageImportMessage = loadMessage("usage.import");
        usageInfoMessage = loadMessage("usage.info");
        usageRemoveMessage = loadMessage("usage.remove");
        usageStorageMessage = loadMessage("usage.storage");
        usageSwapMessage = loadMessage("usage.swap");
        lootAlreadyExistsMessage = loadMessage("loot_already_exists");
        lootNotFoundMessage = loadMessage("loot_not_found");
        lootNotFoundGiveMessage = loadMessage("loot_not_found_give");
        lootCreatedMessage = loadMessage("loot_created");
        lootDeletedMessage = loadMessage("loot_deleted");
        lootImportedMessage = loadMessage("loot_imported");
        playerOnlyMessage = loadMessage("player_only");
        playerNotFoundMessage = loadMessage("player_not_found");
        editingLootMessage = loadMessage("editing_loot");
        invalidNumberFormatMessage = loadMessage("invalid_number_format");
        giveSuccessMessage = loadMessage("give_success");
        giveReceivedMessage = loadMessage("give_received");
        lootInfoHeaderMessage = loadMessage("loot_info_header");
        lootInfoNameMessage = loadMessage("loot_info_name");
        lootInfoItemsMessage = loadMessage("loot_info_items");
        lootListEmptyMessage = loadMessage("loot_list_empty");
        lootListHeaderMessage = loadMessage("loot_list_header");
        lootListEntryMessage = loadMessage("loot_list_entry");
        openingLootsMenuMessage = loadMessage("opening_loots_menu");
        storageSavedMessage = loadMessage("storage_saved");
        storageReloadedMessage = loadMessage("storage_reloaded");
        swapInvalidOffhandMessage = loadMessage("swap_invalid_offhand");
        swapInvalidMainhandMessage = loadMessage("swap_invalid_mainhand");
        swapSameItemMessage = loadMessage("swap_same_item");
        swapItemNotFoundMessage = loadMessage("swap_item_not_found");
        swapSuccessAllMessage = loadMessage("swap_success_all");
        swapSuccessSingleMessage = loadMessage("swap_success_single");
    }

    private String loadMessage(String path) {
        String message = config.getString(path);
        return message == null || message.isBlank() ? "" : StringColorize.parse(message);
    }

    public void sendMessage(CommandSender sender, String message) {
        if (message != null && !message.isBlank()) {
            sender.sendMessage(message);
        }
    }
}
