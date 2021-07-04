package studio.potatocraft.binarysync;

import org.bukkit.plugin.java.JavaPlugin;

public final class BinarySync extends JavaPlugin {
    private static BinarySync instance;

    public BinarySync(){
        instance = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static BinarySync getInstance() {
        return instance;
    }
}
