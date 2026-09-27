package com.bxtpa;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpDenyCommand implements CommandExecutor {

    private final BXTpa plugin;

    public TpDenyCommand(BXTpa plugin) {
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
        plugin.getMessageManager().send(player, "request-denied");

        Player requester = plugin.getServer().getPlayer(request.getSender());
        if (requester != null && requester.isOnline()) {
            plugin.getMessageManager().send(requester, "request-denied");
        }
        return true;
    }
}
