package io.github.btarg.javaOpenAI;

import io.github.btarg.javaOpenAI.listeners.ChatListener;
import io.github.btarg.javaOpenAI.openai.ChatGPTAPI;
import io.github.btarg.javaOpenAI.openai.memory.PersistentChatMemoryStore;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class ChatGPTPlugin extends JavaPlugin {

    @Getter
    private static ChatGPTPlugin instance;
    private PersistentChatMemoryStore persistentChatMemoryStore;
    @Getter
    private ChatGPTAPI chatGPTAPI;

    @Getter
    private FileConfiguration config;

    private final List<String> defaultForbiddenCommands = Arrays.asList("stop", "reload", "kick", "ban", "kill");

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.config = getConfig();

        persistentChatMemoryStore = new PersistentChatMemoryStore();
        chatGPTAPI = new ChatGPTAPI(persistentChatMemoryStore);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }

    public List<String> getForbiddenCommands() {
        if (this.config == null) return defaultForbiddenCommands;
        return Objects.requireNonNullElse(this.config.getStringList("forbidden-commands"), defaultForbiddenCommands);
    }

    @Override
    public void onDisable() {
        // save to file when disabling
        if (persistentChatMemoryStore != null)
            persistentChatMemoryStore.save();
    }
}