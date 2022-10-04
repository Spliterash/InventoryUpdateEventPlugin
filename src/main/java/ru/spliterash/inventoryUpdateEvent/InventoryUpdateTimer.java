package ru.spliterash.inventoryUpdateEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import ru.spliterash.inventoryUpdateEvent.event.PlayerInventoryUpdateAsyncEvent;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.StreamSupport;

public class InventoryUpdateTimer implements Listener {
    private final Map<UUID, ItemStack[]> playerInventories = new HashMap<>();
    private final BukkitTask task;
    private final Lock lock = new ReentrantLock();
    private final JavaPlugin plugin;

    public InventoryUpdateTimer(JavaPlugin plugin) {
        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::tick, 0, 0L);
        this.plugin = plugin;
    }

    private void tick() {
        if (!lock.tryLock())
            return;
        try {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                ItemStack[] prevArray = playerInventories.get(onlinePlayer.getUniqueId());

                PlayerInventory inventory = onlinePlayer.getInventory();
                int size = inventory.getSize();

                ItemStack[] inventoryArray;
                if (prevArray == null)
                    inventoryArray = StreamSupport.stream(inventory.spliterator(), false).map(ItemStack::clone).toArray(ItemStack[]::new);
                else {
                    inventoryArray = new ItemStack[size];

                    List<PlayerInventoryUpdateAsyncEvent.ChangingItem> changes = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        ItemStack newItem = inventory.getItem(i);
                        ItemStack oldItem = prevArray[i];

                        inventoryArray[i] = newItem != null ? newItem.clone() : null;

                        if (!Objects.equals(newItem, oldItem)) {
                            changes.add(new PlayerInventoryUpdateAsyncEvent.ChangingItem(i, oldItem, newItem));
                        }
                    }
                    if (!changes.isEmpty())
                        Bukkit.getPluginManager().callEvent(new PlayerInventoryUpdateAsyncEvent(onlinePlayer, changes));
                }
                playerInventories.put(onlinePlayer.getUniqueId(), inventoryArray);
            }
        } finally {
            lock.unlock();
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (lock.tryLock())
            try {
                playerInventories.remove(e.getPlayer().getUniqueId());
            } finally {
                lock.unlock();
            }
        else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    lock.lock();
                    playerInventories.remove(e.getPlayer().getUniqueId());
                } finally {
                    lock.unlock();
                }
            });
        }
    }


    public void onDisable() {
        task.cancel();
        playerInventories.clear();
    }
}
