package io.github.btarg.javaOpenAI.openai;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import io.github.btarg.javaOpenAI.openai.memory.PersistentChatMemoryStore;
import io.github.btarg.javaOpenAI.openai.tools.Calculator;

public class ChatGPTAPI {

    ChatMemoryStore chatMemoryStore;

    public ChatGPTAPI(ChatMemoryStore chatMemoryStore) {
        this.chatMemoryStore = chatMemoryStore;
    }

    public String GetResponse(String message) {
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(10)
                .chatMemoryStore(new PersistentChatMemoryStore())
                .build();

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(OpenAiChatModel.withApiKey(System.getenv("OPENAI_API_KEY")))
                .tools(new Calculator())
                .chatMemory(chatMemory)
                .build();
        return assistant.chat(message);
    }

    interface Assistant {
        String chat(String userMessage);
    }

}
