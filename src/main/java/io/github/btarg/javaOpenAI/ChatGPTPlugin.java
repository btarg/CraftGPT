package io.github.btarg.javaOpenAI;

import io.github.btarg.javaOpenAI.listeners.ChatListener;
import io.github.btarg.javaOpenAI.openai.ChatGPTAPI;
import io.github.btarg.javaOpenAI.openai.memory.PersistentChatMemoryStore;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChatGPTPlugin extends JavaPlugin {

    private PersistentChatMemoryStore persistentChatMemoryStore;

    @Getter
    private static ChatGPTPlugin instance;

    @Getter
    private ChatGPTAPI chatGPTAPI;

    @Override
    public void onEnable() {
        instance = this;
        persistentChatMemoryStore = new PersistentChatMemoryStore();
        chatGPTAPI = new ChatGPTAPI(persistentChatMemoryStore);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);

    }

    @Override
    public void onDisable() {
        // save to file when disabling
        persistentChatMemoryStore.save();
    }
}