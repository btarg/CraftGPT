package io.github.btarg.javaOpenAI.openai.memory;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import io.github.btarg.javaOpenAI.ChatGPTPlugin;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static dev.langchain4j.data.message.ChatMessageDeserializer.messagesFromJson;
import static dev.langchain4j.data.message.ChatMessageSerializer.messagesToJson;

public class PersistentChatMemoryStore implements ChatMemoryStore {

    private final String filePath = new File(ChatGPTPlugin.getInstance().getDataFolder(), "memory.json").getPath();

    private List<ChatMessage> messages;

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        if (messages == null) {
            try {
                Path path = Paths.get(filePath);
                if (!Files.exists(path)) {
                    Files.createDirectories(path.getParent());
                    Files.createFile(path);
                }
                String json = new String(Files.readAllBytes(path));
                if (!json.isEmpty()) {
                    messages = messagesFromJson(json);
                } else {
                    messages = new ArrayList<>();
                }
            } catch (IOException e) {
                e.printStackTrace();
                messages = new ArrayList<>();
            }
        }
        return messages;
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> newMessages) {
        messages = newMessages;
        save();
    }

    public void save() {
        Bukkit.getScheduler().runTaskAsynchronously(ChatGPTPlugin.getInstance(), () -> {
            try {
                Path path = Paths.get(filePath);
                if (!Files.exists(path)) {
                    Files.createFile(path);
                }
                String json = messagesToJson(messages);
                Files.write(path, json.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void deleteMessages(Object memoryId) {
        Bukkit.getScheduler().runTaskAsynchronously(ChatGPTPlugin.getInstance(), () -> {
            try {
                Files.deleteIfExists(Paths.get(filePath));
                messages = new ArrayList<>();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}