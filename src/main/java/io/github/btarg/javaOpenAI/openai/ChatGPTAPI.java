package io.github.btarg.javaOpenAI.openai;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.UserName;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import io.github.btarg.javaOpenAI.openai.memory.PersistentChatMemoryStore;
import io.github.btarg.javaOpenAI.openai.tools.Calculator;
import io.github.btarg.javaOpenAI.openai.tools.CommandTool;
import io.github.btarg.javaOpenAI.openai.tools.PlayerTool;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatGPTAPI {

    ChatMemoryStore chatMemoryStore;

    public ChatGPTAPI(ChatMemoryStore chatMemoryStore) {
        this.chatMemoryStore = chatMemoryStore;
    }

    public String GetResponse(Player sender, String message) {
        String senderUUID = sender.getUniqueId().toString();

        Object[] tools = new Object[] {
                new Calculator(),
                new CommandTool(),
                new PlayerTool()
        };

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(10)
                .chatMemoryStore(new PersistentChatMemoryStore())
                .build();

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(OpenAiChatModel.withApiKey(System.getenv("OPENAI_API_KEY")))
                .tools(tools)
                .chatMemory(chatMemory)
                .build();
        return assistant.chat(senderUUID, message);
    }

    interface Assistant {
        String chat(@UserName String userUUID, @UserMessage String userMessage);
    }

}
