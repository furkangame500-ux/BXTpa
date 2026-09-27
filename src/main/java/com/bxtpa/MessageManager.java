package com.bxtpa;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageManager {

    private final BXTpa plugin;

    public MessageManager(BXTpa plugin) {
        this.plugin = plugin;
    }

    public String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public String get(String key) {
        String prefix = plugin.getConfig().getString("messages.prefix", "");
        String msg = plugin.getConfig().getString("messages." + key, key);
        return color(msg.replace("%prefix%", prefix));
    }

    public String get(String key, String placeholder, String value) {
        return get(key).replace(placeholder, value);
    }

    public void send(Player player, String key) {
        player.sendMessage(get(key));
    }

    public void send(Player player, String key, String placeholder, String value) {
        player.sendMessage(get(key, placeholder, value));
    }

    /**
     * Sends the request message with clickable ACCEPT / DENY buttons.
     */
    public void sendRequestMessage(Player target, Player sender, boolean tphere) {
        String key = tphere ? "request-received-tphere" : "request-received-tpa";
        target.sendMessage(get(key, "%player%", sender.getName()));

        TextComponent accept = new TextComponent(get("accept-button"));
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"));
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(get("accept-hover"))));

        TextComponent deny = new TextComponent(get("deny-button"));
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny"));
        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(get("deny-hover"))));

        TextComponent spacer = new TextComponent("  ");

        TextComponent line = new TextComponent();
        line.addExtra(accept);
        line.addExtra(spacer);
        line.addExtra(deny);
        target.spigot().sendMessage(line);
    }

    public void sendCountdownTitle(Player player, int seconds) {
        String title = get("countdown", "%seconds%", String.valueOf(seconds));
        String subtitle = get("countdown-warning");
        player.sendTitle(title, subtitle, 0, 25, 0);
    }
}
