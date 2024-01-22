package io.github.btarg.javaOpenAI.commands;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SuppressWarnings("deprecation")
public class GPTSender implements ConsoleCommandSender {
    @Getter
    private final List<String> output = new ArrayList<>();
    private final ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    @Override
    public void sendMessage(@NotNull String s) {
        output.add(s);
        console.sendMessage(s);
    }

    @Override
    public void sendMessage(@NotNull String... strings) {
        Collections.addAll(output, strings);
        console.sendMessage(strings);
    }

    @Override
    public void sendMessage(@Nullable UUID uuid, @NotNull String s) {
        output.add(s);
        console.sendMessage(uuid, s);
    }

    @Override
    public void sendMessage(@Nullable UUID uuid, @NotNull String... strings) {
        Collections.addAll(output, strings);
        console.sendMessage(uuid, strings);
    }

    @Override
    public @NotNull Server getServer() {
        return console.getServer();
    }

    @Override
    public @NotNull String getName() {
        return console.getName();
    }

    @NotNull
    @Override
    public Spigot spigot() {
        return console.spigot();
    }

    @Override
    public @NotNull Component name() {
        return console.name();
    }

    @Override
    public boolean isConversing() {
        return console.isConversing();
    }

    @Override
    public void acceptConversationInput(@NotNull String s) {
        console.acceptConversationInput(s);
    }

    @Override
    public boolean beginConversation(@NotNull Conversation conversation) {
        return console.beginConversation(conversation);
    }

    @Override
    public void abandonConversation(@NotNull Conversation conversation) {
        console.abandonConversation(conversation);
    }

    @Override
    public void abandonConversation(@NotNull Conversation conversation, @NotNull ConversationAbandonedEvent conversationAbandonedEvent) {
        console.abandonConversation(conversation, conversationAbandonedEvent);
    }

    @Override
    public void sendRawMessage(@NotNull String s) {
        console.sendRawMessage(s);
    }

    @Override
    public void sendRawMessage(@Nullable UUID uuid, @NotNull String s) {
        console.sendRawMessage(uuid, s);
    }

    @Override
    public boolean isPermissionSet(@NotNull String s) {
        return console.isPermissionSet(s);
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission permission) {
        return console.isPermissionSet(permission);
    }

    @Override
    public boolean hasPermission(@NotNull String s) {
        return console.hasPermission(s);
    }

    @Override
    public boolean hasPermission(@NotNull Permission permission) {
        return console.hasPermission(permission);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b) {
        return console.addAttachment(plugin, s, b);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin) {
        return console.addAttachment(plugin);
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b, int i) {
        return console.addAttachment(plugin, s, b, i);
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, int i) {
        return console.addAttachment(plugin, i);
    }

    @Override
    public void removeAttachment(@NotNull PermissionAttachment permissionAttachment) {
        console.removeAttachment(permissionAttachment);
    }

    @Override
    public void recalculatePermissions() {
        console.recalculatePermissions();
    }

    @Override
    public @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return console.getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return console.isOp();
    }

    @Override
    public void setOp(boolean b) {
        console.setOp(b);
    }
}