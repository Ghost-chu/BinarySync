package studio.potatocraft.binarysync.event;

import org.bukkit.entity.Player;

public class PlayerSyncEvent extends PlayerSyncEventBase {
    public PlayerSyncEvent(Player player) {
        super(player);
    }
}
