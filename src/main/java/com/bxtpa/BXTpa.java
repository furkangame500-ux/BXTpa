package com.bxtpa;

import org.bukkit.plugin.java.JavaPlugin;

public class BXTpa extends JavaPlugin {

    private static BXTpa instance;
    private RequestManager requestManager;
    private MessageManager messageManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.messageManager = new MessageManager(this);
        this.requestManager = new RequestManager(this);

        getCommand("tpa").setExecutor(new TpaCommand(this, false));
        getCommand("tphere").setExecutor(new TpaCommand(this, true));
        getCommand("tpaccept").setExecutor(new TpAcceptCommand(this));
        getCommand("tpdeny").setExecutor(new TpDenyCommand(this));

        getLogger().info("BXTpa aktif! Hizli. Basit. Ozellestirilebilir.");
    }

    @Override
    public void onDisable() {
        getLogger().info("BXTpa kapatildi.");
    }

    public static BXTpa getInstance() {
        return instance;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }
}
