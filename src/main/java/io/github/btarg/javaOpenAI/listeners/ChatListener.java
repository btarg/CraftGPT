package io.github.btarg.javaOpenAI.listeners;

import io.github.btarg.javaOpenAI.ChatGPTPlugin;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        String playerMessage = event.signedMessage().message();
        String response = ChatGPTPlugin.getInstance().getChatGPTAPI().GetResponse(playerMessage);
        event.getPlayer().sendMessage(Component.text(response));

    }
}