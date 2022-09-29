package ru.spliterash.inventoryUpdateEvent;

import org.bukkit.plugin.java.JavaPlugin;

public class InventoryUpdatePlugin extends JavaPlugin {

    private InventoryUpdateTimer timer;

    @Override
    public void onEnable() {
        timer = new InventoryUpdateTimer(this);
    }

    @Override
    public void onDisable() {
        timer.onDisable();
    }
}
