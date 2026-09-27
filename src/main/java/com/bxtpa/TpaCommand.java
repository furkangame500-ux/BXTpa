package com.bxtpa;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpaCommand implements CommandExecutor {

    private final BXTpa plugin;
    private final boolean tphere;

    public TpaCommand(BXTpa plugin, boolean tphere) {
        this.plugin = plugin;
        this.tphere = tphere;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Bu komut sadece oyuncular icindir.");
            return true;
        }

        if (!player.hasPermission("bxtpa.use")) {
            plugin.getMessageManager().send(player, "no-permission");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(plugin.getMessageManager().color("&cKullanim: /" + label + " <oyuncu>"));
            return true;
        }

        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            plugin.getMessageManager().send(player, "player-not-found");
            return true;
        }

        if (target.getUniqueId().equals(player.getUniqueId())) {
            plugin.getMessageManager().send(player, "cannot-tpa-self");
            return true;
        }

        TpaRequest request = new TpaRequest(player.getUniqueId(), target.getUniqueId(), tphere);
        plugin.getRequestManager().addRequest(request);

        plugin.getMessageManager().send(player, "request-sent", "%player%", target.getName());
        plugin.getMessageManager().sendRequestMessage(target, player, tphere);
        return true;
    }
}
