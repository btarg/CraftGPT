package io.github.btarg.javaOpenAI.openai.tools;

import dev.langchain4j.agent.tool.Tool;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class PlayerTool {
    @Tool("Gets a map of online players, where the key is the player's UUID and the value is their name")
    Map<UUID, String> getOnlinePlayers() {
        return getPlayerAttributeMap(Player::getUniqueId, Player::getName);
    }

    @Tool("Gets a player's name from their UUID")
    String getPlayerNameFromUUID(String uuid) {
        return getPlayerAttribute(uuid, Player::getName, "");
    }

    @Tool("Gets a player's exact XYZ location from their UUID")
    List<Double> getPlayerExactLocation(String uuid) {
        return getPlayerAttribute(uuid, player -> Arrays.asList(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()), new ArrayList<>());
    }

    @Tool("Gets a player's rounded block location from their UUID")
    List<Integer> getPlayerBlockLocation(String uuid) {
        return getPlayerAttribute(uuid, player -> Arrays.asList((int) player.getLocation().getX(), (int) player.getLocation().getY(), (int) player.getLocation().getZ()), new ArrayList<>());
    }

    @Tool("Gets a player's health from their UUID")
    double getPlayerHealth(String uuid) {
        return getPlayerAttribute(uuid, Player::getHealth, 0.0);
    }

    @Tool("Gets a player's food level from their UUID")
    int getPlayerFoodLevel(String uuid) {
        return getPlayerAttribute(uuid, Player::getFoodLevel, 0);
    }

    @Tool("Gets a player's level from their UUID")
    int getPlayerExperienceLevel(String uuid) {
        return getPlayerAttribute(uuid, Player::getLevel, 0);
    }

    @Tool("Gets a player's XP from their UUID")
    int getPlayerExperiencePoints(String uuid) {
        return getPlayerAttribute(uuid, Player::getTotalExperience, 0);
    }


//    @Tool("Gets a player's inventory contents by slot from their UUID")
//    Map<String, String> getPlayerInventory(String uuid) {
//        return getPlayerAttribute(uuid, player -> {
//            PlayerInventory inventory = player.getInventory();
//            Map<String, String> items = new HashMap<>();
//
//            for (int i = 0; i < inventory.getSize(); i++) {
//                ItemStack item = inventory.getItem(i);
//                if (item != null && item.getType() != Material.AIR) {
//                    items.put("Slot " + i, item.getType().name());
//                }
//            }
//
//            items.values().removeIf(name -> name.equals(Material.AIR.name()));
//            return items;
//        }, new HashMap<>());
//    }

    @Tool("Gets a player's hotbar contents by slot from their UUID")
    Map<String, String> getPlayerHotbar(String uuid) {
        return getPlayerAttribute(uuid, player -> {
            PlayerInventory inventory = player.getInventory();
            Map<String, String> items = new HashMap<>();

            for (int i = 0; i < 9; i++) {
                ItemStack item = inventory.getItem(i);
                if (item != null && item.getType() != Material.AIR) {
                    items.put("Slot " + i, item.getType().name());
                }
            }

            items.values().removeIf(name -> name.equals(Material.AIR.name()));
            return items;
        }, new HashMap<>());
    }

    @Tool("Gets the item held in a player's main hand from their UUID")
    String getPlayerMainHand(String uuid) {
        return getPlayerAttribute(uuid, player -> getItemTypeName(player.getInventory().getItemInMainHand()), Material.AIR.name());
    }

    @Tool("Gets the item held in a player's offhand from their UUID")
    String getPlayerOffHand(String uuid) {
        return getPlayerAttribute(uuid, player -> getItemTypeName(player.getInventory().getItemInOffHand()), Material.AIR.name());
    }

    @Tool("Gets a player's armor contents by slot from their UUID")
    Map<String, String> getPlayerArmor(String uuid) {
        return getPlayerAttribute(uuid, player -> {
            PlayerInventory inventory = player.getInventory();
            Map<String, String> items = new HashMap<>();

            items.put("Helmet", getItemTypeName(inventory.getHelmet()));
            items.put("Chestplate", getItemTypeName(inventory.getChestplate()));
            items.put("Leggings", getItemTypeName(inventory.getLeggings()));
            items.put("Boots", getItemTypeName(inventory.getBoots()));

            items.values().removeIf(name -> name.equals(Material.AIR.name()));
            return items;
        }, new HashMap<>());
    }

    private String getItemTypeName(ItemStack item) {
        return item == null ? Material.AIR.name() : item.getType().name();
    }

    private <T> T getPlayerAttribute(String uuid, Function<Player, T> attributeGetter, T defaultValue) {
        Player player = Bukkit.getPlayer(UUID.fromString(uuid));
        if (player == null) return defaultValue;
        return attributeGetter.apply(player);
    }

    private <K, V> Map<K, V> getPlayerAttributeMap(Function<Player, K> keyGetter, Function<Player, V> valueGetter) {
        return Bukkit.getOnlinePlayers().stream()
                .collect(Collectors.toMap(keyGetter, valueGetter));
    }
}
