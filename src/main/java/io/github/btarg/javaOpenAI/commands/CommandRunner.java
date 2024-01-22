package io.github.btarg.javaOpenAI.commands;

import io.github.btarg.javaOpenAI.ChatGPTPlugin;
import org.bukkit.Bukkit;

import java.util.concurrent.ExecutionException;

import java.util.function.Consumer;
import java.util.HashMap;
import java.util.Map;

public class CommandRunner {
    public static void runCommand(String command, Consumer<Map<String, Object>> callback) throws ExecutionException, InterruptedException {
        GPTSender sender = new GPTSender();
        boolean result = Bukkit.getScheduler().callSyncMethod(ChatGPTPlugin.getInstance(), () -> Bukkit.dispatchCommand(sender, command)).get();

        Map<String, Object> callbackData = new HashMap<>();
        callbackData.put("output", sender.getOutput());
        callbackData.put("result", result);

        callback.accept(callbackData);
    }
}