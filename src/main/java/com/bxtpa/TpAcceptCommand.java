package com.bxtpa;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpAcceptCommand implements CommandExecutor {

    private final BXTpa plugin;

    public TpAcceptCommand(BXTpa plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Bu komut sadece oyuncular icindir.");
            return true;
        }

        TpaRequest request = plugin.getRequestManager().getRequest(player.getUniqueId());
        if (request == null) {
            plugin.getMessageManager().send(player, "no-pending-request");
            return true;
        }

        plugin.getRequestManager().removeRequest(player.getUniqueId());

        Player requester = plugin.getServer().getPlayer(request.getSender());
        if (requester == null || !requester.isOnline()) {
            plugin.getMessageManager().send(player, "player-not-found");
            return true;
        }

        plugin.getMessageManager().send(player, "request-accepted");
        plugin.getMessageManager().send(requester, "request-accepted");

        // tpa: requester teleports to target. tphere: target teleports to requester.
        Player teleporter = request.isTphere() ? player : requester;
        Player destination = request.isTphere() ? requester : player;

        int seconds = plugin.getConfig().getInt("settings.countdown-seconds", 5);
        new CountdownTask(plugin, teleporter, destination).start(seconds);
        return true;
    }
}
