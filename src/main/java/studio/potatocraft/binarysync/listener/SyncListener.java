package studio.potatocraft.binarysync.listener;

import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import studio.potatocraft.binarysync.sync.SyncManager;

import java.util.UUID;
@AllArgsConstructor
public class SyncListener extends ListenerBase {
    private final SyncManager syncManager;
    @EventHandler(priority = EventPriority.LOWEST)
    public void fetchPlayerData(AsyncPlayerPreLoginEvent event){
        UUID uuid = event.getUniqueId();
        // Block the thread here, we don't want player join the server without data fetched.
        if(!syncManager.fetchPlayerCache(uuid)){
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,"Failed to fetch player data, please contact with server administrator.");
        }
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void checkFetchPlayerDataIfNeeded(AsyncPlayerPreLoginEvent event){
        if(event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED){
            // Other plugin blocked player join in, so we don't need sync him data
            syncManager.invalidPlayerCache(event.getUniqueId());
        }
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void applyPlayerData(PlayerJoinEvent event){
        syncManager.playerJoin(event.getPlayer());
        syncManager.invalidPlayerCache(event.getPlayer().getUniqueId());
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void invalidatingPlayerData(PlayerQuitEvent event){
        syncManager.playerLeft(event.getPlayer());
        syncManager.invalidPlayerCache(event.getPlayer().getUniqueId());
    }
}
