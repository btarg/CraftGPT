package io.github.btarg.javaOpenAI.openai.tools;

import dev.langchain4j.agent.tool.Tool;
import io.github.btarg.javaOpenAI.ChatGPTPlugin;
import org.bukkit.Bukkit;
public class CommandTool {

    @Tool("Executes a registered Minecraft command")
    void executeCommand(String command) {
        // strip a slash if it exists
        if (command.startsWith("/")) {
            command = command.substring(1);
        }
        String finalCommand = command;
        Bukkit.getScheduler().runTask(ChatGPTPlugin.getInstance(), () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand);
        });
    }
}