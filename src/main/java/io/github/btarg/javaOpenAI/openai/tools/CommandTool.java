package io.github.btarg.javaOpenAI.openai.tools;

import dev.langchain4j.agent.tool.Tool;
import io.github.btarg.javaOpenAI.ChatGPTPlugin;
import io.github.btarg.javaOpenAI.commands.CommandRunner;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class CommandTool {

    public static final String COMMAND_SUCCESS = "Command ran successfully: ";
    public static final String COMMAND_FAILURE = "Command failed to run: ";
    public static final String COMMAND_FORBIDDEN = "Command is forbidden";
    public static final String NO_PERMISSION = "Player does not have permission to run this command";

    @Tool("Executes a Minecraft command as the specified player. Use @s to reference the player, e.g. /give @s diamond")
    String executeCommand(String playerUUID, String command) throws ExecutionException, InterruptedException {
        // strip a slash if it exists
        if (command.startsWith("/")) {
            command = command.substring(1);
        }

        // get the first part of the command
        String commandName = command.split(" ")[0];
        if (getForbiddenCommands().contains(commandName)) {
            // log we tried to execute a forbidden command
            ChatGPTPlugin.getInstance().getLogger().warning("Tried to execute a forbidden command: " + commandName);
            return COMMAND_FORBIDDEN;
        }
        // execute as the player
        command = "execute as " + playerUUID + " run " + command;

        Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));
        if (player != null) {
            // Check if the player has permission to execute the command
            if (!player.hasPermission(commandName)) {
                return NO_PERMISSION;
            }

            AtomicReference<String> finalOutput = new AtomicReference<>("");
            finalOutput.set(COMMAND_FAILURE);

            ChatGPTPlugin.getInstance().getLogger().info("Executing command '" + command + "' as player '" + player.getName() + "'");
            CommandRunner.runCommand(command, (callbackData) -> {
                ChatGPTPlugin.getInstance().getLogger().info("Command output: " + callbackData.get("output"));
                if ((boolean) callbackData.get("result")) {
                     finalOutput.set(COMMAND_SUCCESS + callbackData.get("output"));
                } else {
                    finalOutput.set(COMMAND_FAILURE + callbackData.get("output"));
                }
            });
            return finalOutput.get();
        }
        return COMMAND_FAILURE;
    }

    @Tool("Gets a list of all Materials (item or block) in Minecraft")
    List<String> getMaterialNames() {
        List<String> materials = new ArrayList<>();
        for (Material material : Material.values()) {
            materials.add(material.name().toLowerCase());
        }
        return materials;
    }
    @Tool("Checks if a Material (item or block) exists in Minecraft")
    boolean materialExists(String materialName) {
        return Material.getMaterial(materialName.toUpperCase().trim()) != null;
    }


    @Tool("Gets a list of all forbidden Minecraft commands")
    List<String> getForbiddenCommands() {
        return ChatGPTPlugin.getInstance().getForbiddenCommands();
    }

    @Tool("Gets a list of all registered Minecraft commands")
    List<String> getCommands() {
        return Bukkit.getCommandAliases().keySet().stream().toList();
    }
}