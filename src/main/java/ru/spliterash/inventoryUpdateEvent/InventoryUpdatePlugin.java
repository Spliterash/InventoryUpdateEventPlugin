package ru.spliterash.inventoryUpdateEvent;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class InventoryUpdatePlugin extends JavaPlugin {

    private InventoryUpdateTimer timer;

    @Override
    public void onEnable() {
        timer = new InventoryUpdateTimer(this);
        Bukkit.getPluginManager().registerEvents(timer, this);
    }

    @Override
    public void onDisable() {
        timer.onDisable();
    }
}
