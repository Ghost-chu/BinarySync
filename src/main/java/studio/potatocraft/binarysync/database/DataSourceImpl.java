package studio.potatocraft.binarysync.database;

import studio.potatocraft.binarysync.database.exception.PlayerLockFailException;

import java.util.Map;
import java.util.UUID;

public class DataSourceImpl implements DataSource {





    /**
     * Getting specific player row.
     *
     * @param uuid       The player unique id
     * @param playerLock The player data access lock (player lock)
     * @return Map<ColumnName, ColumnData>
     */
    @Override
    public Map<String, String> getRow(UUID uuid, String playerLock) throws PlayerLockFailException {
        return null;
    }

    /**
     * Setting specific player row data
     *
     * @param uuid       The player unique id
     * @param data       Map<ColumnName, ColumnData>
     * @param playerLock The player data access lock (player lock)
     */
    @Override
    public void setRow(UUID uuid, Map<String, String> data, String playerLock) throws PlayerLockFailException {

    }

    /**
     * Mark player has been locked
     *
     * @param uuid          Player unique id
     * @param playerLock    The player data access lock (player lock)
     * @param newPlayerLock The new player data access lock (player lock)
     * @param force         Force set player lock, skip player current lock holding check
     * @throws PlayerLockFailException Throw out this exception when failed to lock the row (player lock)
     */
    @Override
    public void setPlayerLock(UUID uuid, String playerLock, String newPlayerLock, boolean force) throws PlayerLockFailException {

    }

    /**
     * Checks if player has been locked and we had the player lock
     *
     * @param uuid       The player unique id
     * @param playerLock The player data access lock
     * @return The player's player lock is null or matches with data access lock
     */
    @Override
    public boolean holdingPlayerLock(UUID uuid, String playerLock) {
        return false;
    }
}
