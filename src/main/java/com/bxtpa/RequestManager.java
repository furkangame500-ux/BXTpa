package com.bxtpa;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RequestManager {

    private final BXTpa plugin;
    // target UUID -> request
    private final Map<UUID, TpaRequest> requests = new HashMap<>();

    public RequestManager(BXTpa plugin) {
        this.plugin = plugin;
    }

    public void addRequest(TpaRequest request) {
        requests.put(request.getTarget(), request);
    }

    public TpaRequest getRequest(UUID target) {
        TpaRequest request = requests.get(target);
        if (request == null) return null;

        int expire = plugin.getConfig().getInt("settings.request-expire-seconds", 60);
        if (request.isExpired(expire)) {
            requests.remove(target);
            Player player = plugin.getServer().getPlayer(target);
            if (player != null) {
                plugin.getMessageManager().send(player, "request-expired");
            }
            return null;
        }
        return request;
    }

    public void removeRequest(UUID target) {
        requests.remove(target);
    }
}
