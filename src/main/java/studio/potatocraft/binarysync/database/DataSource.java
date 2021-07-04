package studio.potatocraft.binarysync.database;

import studio.potatocraft.binarysync.database.exception.PlayerLockFailException;

import java.util.Map;
import java.util.UUID;
// PlayerLock = UUID#Timestamp
public interface DataSource {
    /**
     * Getting specific player row.
     * @param uuid The player unique id
     * @param playerLock The player data access lock (player lock)
     * @return Map<ColumnName, ColumnData>
     */
    Map<String, String> getRow(UUID uuid, String playerLock) throws PlayerLockFailException;

    /**
     * Setting specific player row data
     * @param uuid The player unique id
     * @param playerLock The player data access lock (player lock)
     * @param data Map<ColumnName, ColumnData>
     */
    void setRow(UUID uuid, Map<String, String> data, String playerLock) throws PlayerLockFailException;

    /**
     * Mark player has been locked
     * @param uuid Player unique id
     * @param playerLock The player data access lock (player lock)
     * @param newPlayerLock The new player data access lock (player lock)
     * @param force Force set player lock, skip player current lock holding check
     * @throws PlayerLockFailException Throw out this exception when failed to lock the row (player lock)
     */
    void setPlayerLock(UUID uuid, String playerLock, String newPlayerLock, boolean force) throws PlayerLockFailException;

    /**
     * Checks if player has been locked and we had the player lock
     * @param uuid The player unique id
     * @param playerLock The player data access lock
     * @return The player's player lock is null or matches with data access lock
     */
    boolean holdingPlayerLock(UUID uuid, String playerLock);
}
