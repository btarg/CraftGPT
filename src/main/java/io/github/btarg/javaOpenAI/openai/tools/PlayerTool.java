package io.github.btarg.javaOpenAI.openai.tools;

import dev.langchain4j.agent.tool.Tool;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PlayerTool {
    @Tool("Gets a map of online players, where the key is the player's UUID and the value is their name")
    Map<UUID, String> getOnlinePlayers() {
        return getPlayerAttributeMap(Player::getUniqueId, Player::getName);
    }

    @Tool("Gets a player's name from their UUID")
    String getPlayerNameFromUUID(String uuid) {
        return getPlayerAttribute(uuid, Player::getName, "");
    }

    @Tool("Gets a player's exact X,Y and Z location from their UUID")
    List<Double> getPlayerExactLocation(String uuid) {
        return getPlayerAttribute(uuid, player -> Arrays.asList(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()), new ArrayList<>());
    }

    @Tool("Gets a player's block location from their UUID")
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

    @Tool("Gets a player's experience level from their UUID")
    int getPlayerExperienceLevel(String uuid) {
        return getPlayerAttribute(uuid, Player::getLevel, 0);
    }

    @Tool("Gets a player's experience points from their UUID")
    int getPlayerExperiencePoints(String uuid) {
        return getPlayerAttribute(uuid, Player::getTotalExperience, 0);
    }

    @Tool("Gets a player's inventory contents with slot information")
    Map<String, ItemStack> getPlayerInventory(String uuid) {
        return getPlayerAttribute(uuid, player -> {
            PlayerInventory inventory = player.getInventory();
            Map<String, ItemStack> items = new HashMap<>();

            items.put("MainHand", inventory.getItemInMainHand());
            items.put("OffHand", inventory.getItemInOffHand());
            items.put("Helmet", inventory.getHelmet());
            items.put("Chestplate", inventory.getChestplate());
            items.put("Leggings", inventory.getLeggings());
            items.put("Boots", inventory.getBoots());

            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack item = inventory.getItem(i);
                if (item != null && !items.containsValue(item)) {
                    items.put("Slot " + i, item);
                }
            }

            return items;
        }, new HashMap<>());
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