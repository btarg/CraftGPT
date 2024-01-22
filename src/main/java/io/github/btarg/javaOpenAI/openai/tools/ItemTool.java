package io.github.btarg.javaOpenAI.openai.tools;

import dev.langchain4j.agent.tool.Tool;
import io.github.btarg.javaOpenAI.util.ComponentHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ItemTool {

    @Tool("Give the player a basic item stack with the specified material and amount")
    public String givePlayerItem(String playerUUID, String material, int amount) {
        Material materialType = Material.getMaterial(material.toUpperCase().trim());
        if (materialType != null) {
            ItemStack stack = new ItemStack(materialType, amount);
            Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));
            if (player != null) {
                player.getInventory().addItem(stack);

                int slot = -1;
                PlayerInventory inventory = player.getInventory();
                for (int i = 0; i < inventory.getSize(); i++) {
                    ItemStack itemInSlot = inventory.getItem(i);
                    if (itemInSlot != null && itemInSlot.isSimilar(stack)) {
                        slot = i;
                        break;
                    }
                }

                if (slot != -1) {
                    return "Gave player " + stack.getType().name() + " x" + stack.getAmount() + " in slot " + slot;
                }
            }
        }
        return "Failed to give item";
    }

    @Tool("Enchant an item stack in the player's inventory")
    void enchantItem(String playerUUID, int slot, Map<String, Integer> itemEnchantments) {
        Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));
        if (player != null) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (stack != null && itemEnchantments != null) {
                for (String enchantmentName : itemEnchantments.keySet()) {
                    Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentName.toLowerCase()));
                    if (enchantment != null) {
                        stack.addEnchantment(enchantment, itemEnchantments.get(enchantmentName));
                    }
                }
            }
        }
    }

    @Tool("Rename an item in the player's inventory")
    void renameItem(String playerUUID, int slot, String itemDisplayName) {
        Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));
        if (player != null) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (stack != null) {
                ItemMeta meta = stack.getItemMeta();
                if (itemDisplayName != null && !itemDisplayName.isBlank())
                    meta.displayName(ComponentHelper.deserializeGenericComponent(itemDisplayName));
                stack.setItemMeta(meta);
            }
        }
    }
}