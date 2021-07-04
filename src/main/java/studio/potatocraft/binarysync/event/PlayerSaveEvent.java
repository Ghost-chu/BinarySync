package studio.potatocraft.binarysync.event;

import org.bukkit.entity.Player;

public class PlayerSaveEvent extends PlayerSyncEventBase {
    public PlayerSaveEvent(Player player) {
        super(player);
    }
}
