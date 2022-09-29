package ru.spliterash.inventoryUpdateEvent.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Getter
public class PlayerInventoryUpdateAsyncEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    private final List<ChangingItem> changedItems;

    public PlayerInventoryUpdateAsyncEvent(@NotNull Player who, List<ChangingItem> changedItems) {
        super(who, true);
        this.changedItems = changedItems;
    }


    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public record ChangingItem(int slot, @Nullable ItemStack oldItem, @Nullable ItemStack newItem) {
    }
}
