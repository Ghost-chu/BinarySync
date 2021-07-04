package studio.potatocraft.binarysync.sync;

import org.bukkit.plugin.Plugin;

public interface SyncModule {
    String getModuleName();
    Plugin getModulePluginInstance();
}
