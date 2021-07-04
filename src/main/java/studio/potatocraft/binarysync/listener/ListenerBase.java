package studio.potatocraft.binarysync.listener;

import org.bukkit.event.Listener;
import studio.potatocraft.binarysync.BinarySync;

public class ListenerBase implements Listener {

    public void register(){
        BinarySync.getInstance().getServer().getPluginManager().registerEvents(this,BinarySync.getInstance());
    }
}
