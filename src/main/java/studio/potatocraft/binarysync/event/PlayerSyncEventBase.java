package studio.potatocraft.binarysync.event;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import studio.potatocraft.binarysync.BinarySync;
@AllArgsConstructor
public abstract class PlayerSyncEventBase extends SyncEventBase {
    protected Player player;

    @NotNull
    public Player getPlayer(){
        return this.player;
    }



}
