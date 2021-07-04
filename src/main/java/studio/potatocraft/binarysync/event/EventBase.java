package studio.potatocraft.binarysync.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import studio.potatocraft.binarysync.BinarySync;

public class EventBase extends Event {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public void callEvent() {
        BinarySync.getInstance().getServer().getPluginManager().callEvent(this);
    }

}
