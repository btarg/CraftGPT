package io.github.btarg.javaOpenAI;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static dev.langchain4j.data.message.ChatMessageDeserializer.messagesFromJson;
import static dev.langchain4j.data.message.ChatMessageSerializer.messagesToJson;

public class PersistentChatMemoryStore implements ChatMemoryStore {

    private final String filePath = "E:\\Ai shenanigans\\chat-memory.json";

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            String json = new String(Files.readAllBytes(path));
            if (json.isEmpty()) {
                return new ArrayList<>();
            }
            return messagesFromJson(json);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
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
    }

    @Override
    public void deleteMessages(Object memoryId) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}