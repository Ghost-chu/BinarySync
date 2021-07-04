package studio.potatocraft.binarysync.module;

import com.google.gson.Gson;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import studio.potatocraft.binarysync.BinarySync;
import studio.potatocraft.binarysync.sync.Sync;
import studio.potatocraft.binarysync.sync.SyncModule;
import studio.potatocraft.binarysync.sync.SyncType;

import java.util.Collection;

public class PlayerDataModule implements SyncModule {
    @Override
    public String getModuleName() {
        return "Player Data";
    }

    @Override
    public Plugin getModulePluginInstance() {
        return BinarySync.getInstance();
    }

    @Sync(field = "health", type = SyncType.SAVE)
    public double savePlayerHeath(Player player) {
        return player.getHealth();
    }

    @Sync(field = "food", type = SyncType.SAVE)
    public int savePlayerFood(Player player) {
        Gson gson;
        return player.getFoodLevel();
    }

    @Sync(field = "experience", type = SyncType.SAVE)
    public int savePlayerExp(Player player) {
        return player.getTotalExperience();
    }
    @Sync(field = "potioneffect", type = SyncType.SAVE)
    public Collection<PotionEffect> savePlayerPotionEffect(Player player) {
        return player.getActivePotionEffects();
    }
    @Sync(field = "inventory", type = SyncType.SAVE)
    public ItemStack[] savePlayerInventory(Player player) {
        return player.getInventory().getContents();
    }
    @Sync(field = "armor", type = SyncType.SAVE)
    public ItemStack[] savePlayerArmor(Player player) {
        return player.getInventory().getArmorContents();
    }
    @Sync(field = "enderchest", type = SyncType.SAVE)
    public ItemStack[] savePlayerEnderChest(Player player) {
        return player.getEnderChest().getContents();
    }


    @Sync(field = "health", type = SyncType.READ)
    public void readPlayerHeath(Player player, double health) {
        player.setHealth(health);
    }

    @Sync(field = "food", type = SyncType.READ)
    public void readPlayerFood(Player player, int food) {
        player.setFoodLevel(food);
    }

    @Sync(field = "experience", type = SyncType.READ)
    public void readPlayerExp(Player player, int experience) {
        player.setTotalExperience(experience);
    }

    @Sync(field = "inventory", type = SyncType.READ)
    public void readPlayerInventory(Player player,ItemStack[] inventory) {
        player.getInventory().setContents(inventory);
    }
    @Sync(field = "armor", type = SyncType.READ)
    public void readPlayerArmor(Player player, ItemStack[] armors) {
        player.getInventory().setArmorContents(armors);
    }
    @Sync(field = "enderchest", type = SyncType.READ)
    public void readPlayerEnderChest(Player player, ItemStack[] enderChest) {
        player.getEnderChest().setContents(enderChest);
    }


}
