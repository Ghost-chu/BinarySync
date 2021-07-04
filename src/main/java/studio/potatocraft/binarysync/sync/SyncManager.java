package studio.potatocraft.binarysync.sync;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SyncManager {
    private final Map<String, Set<SyncModule>> registeredModules = new HashMap<>();
    private final Gson gson = new Gson();
    private final Cache<UUID, Map<String, String>> databaseFetchPool = CacheBuilder.newBuilder()
            .expireAfterAccess(24, TimeUnit.HOURS)
            .build();



    public boolean fetchPlayerCache(UUID uuid){

    }

    public void invalidPlayerCache(UUID uuid){
        databaseFetchPool.invalidate(uuid);
    }

    public boolean playerJoin(Player player){
        UUID uuid = player.getUniqueId();

    }

    public void playerLeft(Player player){

    }


    public void register(Plugin plugin, SyncModule module){
       Set<SyncModule> modules = this.registeredModules.getOrDefault(plugin.getName(),new HashSet<>());
       modules.add(module);
       registeredModules.put(plugin.getName(),modules);
    }

    public void unregister(Plugin plugin, SyncModule module){
        if(this.registeredModules.containsKey(plugin.getName())){
            Set<SyncModule> modules = this.registeredModules.get(plugin.getName());
            modules.remove(module);
        }
    }

    public void unregister(Plugin plugin){
        this.registeredModules.remove(plugin.getName());
    }

    /**
     * Sync player data from database
     * @param player The player pending for sync
     * @param queryResult The database query result:
     *                    Key, Data
     *
     *                    Key -> Namespace.Field
     *                    Data -> Base64 encoded json string
     */
    public void sync(Player player, Map<String, String> queryResult){
        for (Map.Entry<String, String> result : queryResult.entrySet()) {
            String[] temp = result.getKey().split("\\.");
            if(temp.length != 2){
                //TODO: Throw illegal argument exception
                continue;
            }
            String namespace = temp[0];
            String field = temp[1];
            String encodedData = result.getValue();
            String jsonData = new String(Base64.getDecoder().decode(encodedData), StandardCharsets.UTF_8);
            fireSync(player, namespace,field,jsonData);
        }
    }

    /**
     * Save player data to database
     * @param player The player pending for save
     * @return Save result
     */
    public Map<String, String> save(Player player){
        return fireSave(player);
    }

    private Map<String, String> fireSave(Player player){
        Map<String,String> dataMap = new HashMap<>();
        for (Map.Entry<String, Set<SyncModule>> value : this.registeredModules.entrySet()) {
            for (SyncModule syncModule : value.getValue()) {
                for (Method method : syncModule.getClass().getDeclaredMethods()) {
                    Sync syncAnnotation = method.getAnnotation(Sync.class);
                    if(syncAnnotation == null){
                        continue;
                    }
                    if(method.getModifiers() != Modifier.PUBLIC){
                        continue;
                    }
                    if(method.getParameterCount() != 1){
                        continue;
                    }
                    if(method.getParameterTypes()[0] != Player.class){
                        continue;
                    }
                    try {
                        Object data = method.invoke(syncModule,player);
                        String jsonData = gson.toJson(data);
                        String encodedData = Base64.getEncoder().encodeToString(jsonData.getBytes(StandardCharsets.UTF_8));
                        String key = value.getKey()+"."+syncAnnotation.field();
                        dataMap.put(key, encodedData);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        //TODO: Make a logger message
                    }
                }
            }
        }
       return dataMap;
    }

    private void fireSync(Player player, String namespace, String field, String jsonData){
        if(!this.registeredModules.containsKey(namespace)){
            return;
        }
        Set<SyncModule> syncModules = this.registeredModules.get(namespace);

        for (SyncModule syncModule : syncModules) {
            for (Method method : syncModule.getClass().getDeclaredMethods()) {
                Sync syncAnnotation = method.getAnnotation(Sync.class);
                if(syncAnnotation == null){
                    continue;
                }
                if(!syncAnnotation.field().equals(field)){
                    continue;
                }
                if(method.getModifiers() != Modifier.PUBLIC){
                    continue;
                }
                if(method.getParameterCount() != 2){
                    continue;
                }
                if(method.getParameterTypes()[0] != Player.class){
                    continue;
                }
                Class<?> clazzType = method.getParameterTypes()[1];
                try {
                    method.invoke(syncModule, player, gson.fromJson(jsonData,clazzType));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    //TODO: Make a logger message
                }
            }
        }
    }



}
