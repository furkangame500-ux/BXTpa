package com.bxtpa;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CountdownTask {

    private final BXTpa plugin;
    private final Player teleporter;
    private final Player destination;

    public CountdownTask(BXTpa plugin, Player teleporter, Player destination) {
        this.plugin = plugin;
        this.teleporter = teleporter;
        this.destination = destination;
    }

    public void start(int seconds) {
        final Location startLocation = teleporter.getLocation().clone();
        final boolean cancelOnMove = plugin.getConfig().getBoolean("settings.cancel-on-move", true);

        new BukkitRunnable() {
            int remaining = seconds;

            @Override
            public void run() {
                if (!teleporter.isOnline() || !destination.isOnline()) {
                    cancel();
                    return;
                }

                // Cancel if the player moved
                if (cancelOnMove && moved(teleporter.getLocation(), startLocation)) {
                    plugin.getMessageManager().send(teleporter, "teleport-cancelled-move");
                    cancel();
                    return;
                }

                if (remaining <= 0) {
                    teleporter.teleport(destination.getLocation());
                    plugin.getMessageManager().send(teleporter, "teleported");
                    cancel();
                    return;
                }

                plugin.getMessageManager().sendCountdownTitle(teleporter, remaining);
                remaining--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private boolean moved(Location current, Location start) {
        if (current.getWorld() != start.getWorld()) return true;
        double dx = current.getX() - start.getX();
        double dy = current.getY() - start.getY();
        double dz = current.getZ() - start.getZ();
        return (dx * dx + dy * dy + dz * dz) > 0.04; // ~0.2 block tolerance
    }
}
