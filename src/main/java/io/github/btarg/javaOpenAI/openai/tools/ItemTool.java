package io.github.btarg.javaOpenAI.openai.tools;

import dev.langchain4j.agent.tool.Tool;
import io.github.btarg.javaOpenAI.util.ComponentHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ItemTool {
    @Tool("Give the player an item with the specified material, amount and custom metadata")
    void giveItem(String playerUUID, String material, int amount, String itemDisplayName, List<String> itemLore, Map<String, Integer> itemEnchantments) {
        Material materialType = Material.getMaterial(material.toUpperCase().trim());
        if (materialType != null) {
            ItemStack stack = new ItemStack(materialType, amount);
            // load persistent metadata
            ItemMeta meta = stack.getItemMeta();
            // set the metadata
            meta.displayName(ComponentHelper.deserializeGenericComponent(itemDisplayName));
            for (String loreString : itemLore) {
                Objects.requireNonNull(meta.lore()).add(ComponentHelper.deserializeGenericComponent(loreString));
            }
            stack.setItemMeta(meta);
            for (String enchantmentName : itemEnchantments.keySet()) {
                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentName.toLowerCase()));
                if (enchantment != null) {
                    stack.addEnchantment(enchantment, itemEnchantments.get(enchantmentName));
                }
            }


            // get player by uuid and give them the item
            Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));
            if (player != null) {
                player.getInventory().addItem(stack);
            }
        }
    }
}
