package io.github.btarg.javaOpenAI.openai.tools;

import dev.langchain4j.agent.tool.Tool;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerTool {
    @Tool("Gets a map of online players, where the key is the player's UUID and the value is their name")
    Map<UUID, String> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().stream()
                .collect(Collectors.toMap(Player::getUniqueId, Player::getName));
    }
    @Tool("Gets a player's name from their UUID")
    String getPlayerNameFromUUID(String uuid) {
        Player player = Bukkit.getPlayer(UUID.fromString(uuid));
        if (player == null) return "";
        return player.getName();
    }

    @Tool("Gets a player's X,Y and Z position from their UUID")
    List<Double> getPlayerPosition(String uuid) {
        Player player = Bukkit.getPlayer(UUID.fromString(uuid));
        if (player == null) return new ArrayList<>();
        List<Double> position = new ArrayList<>();
        position.add(player.getLocation().getX());
        position.add(player.getLocation().getY());
        position.add(player.getLocation().getZ());
        return position;
    }

    @Tool("Gets a player's inventory contents as strings")
    List<String> getPlayerInventory(String uuid) {
        Player player = Bukkit.getPlayer(UUID.fromString(uuid));
        if (player == null) return new ArrayList<>();

        List<String> inventoryContents = new ArrayList<>();
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                // itemNBT is a string which represents the item like so:
                // "diamond_sword x1 {display:{Name:'{"text":"Sword of the Gods"}'}}"
                int amount = item.getAmount();
                String itemNBT = item.getType() + " x" + amount + " " + item.serialize();

                inventoryContents.add(itemNBT);
            }
        }
        return inventoryContents;
    }
}