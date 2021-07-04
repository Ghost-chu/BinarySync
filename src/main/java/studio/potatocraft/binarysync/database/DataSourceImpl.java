package studio.potatocraft.binarysync.database;

import org.jetbrains.annotations.NotNull;
import studio.potatocraft.binarysync.database.exception.PlayerLockFailException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DataSourceImpl implements DataSource {
    private final static String tableName = "binarysync_data";
    private final String databaseName;
    private final DatabaseCore core;

    public DataSourceImpl(DatabaseCore core, String databaseName) {
        this.core = core;
        this.databaseName = databaseName;
        setupTable();
    }

    private void setupTable() {

    }

    /**
     * Returns true if the table exists
     *
     * @param table The table to check for
     * @return True if the table is found
     * @throws SQLException Throw exception when failed execute somethins on SQL
     */
    boolean hasTable(@NotNull String table) throws SQLException {
        boolean match = false;
        try (ResultSet rs = core.getConnection().getMetaData().getTables(null, null, "%", null)) {
            while (rs.next()) {
                if (table.equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
                    match = true;
                    break;
                }
            }
        }
        return match;
    }

    /**
     * Returns true if the given table has the given column
     *
     * @param table  The table
     * @param column The column
     * @return True if the given table has the given column
     * @throws SQLException If the database isn't connected
     */
    public boolean hasColumn(@NotNull String table, @NotNull String column) throws SQLException {
        if (!hasTable(table)) {
            return false;
        }

        String query = "SELECT * FROM " + table + " LIMIT 1";
        boolean match = false;
        try (PreparedStatement ps = core.getConnection().prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            ResultSetMetaData metaData = rs.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                if (metaData.getColumnLabel(i).equals(column)) {
                    match = true;
                    break;
                }
            }
        } catch (SQLException e) {
            return match;
        }
        return match; // Uh, wtf.
    }

    private void createBasicTable() {
        String sql = "CREATE TABLE `asaasa`.`Untitled`  (\n" +
                "  `uuid` varchar(64) NOT NULL,\n" +
                "  `lock` varchar(255) NOT NULL,\n" +
                "  PRIMARY KEY (`uuid`),\n" +
                "  UNIQUE INDEX `uuidindex`(`uuid`) USING HASH\n" +
                ");";
        try (PreparedStatement preparedStatement = new BufferStatement(sql).prepareStatement(core.getConnection())) {
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private List<String> getColumns() throws SQLException {
        List<String> columns = new ArrayList<>();
        String sql = "select COLUMN_NAME from information_schema.COLUMNS where table_name = '" + tableName + "' and table_schema = '" + databaseName + "';";

        try (ResultSet resultSet = new BufferStatement(sql).prepareStatement(core.getConnection()).executeQuery()) {
            while (resultSet.next()) {
                columns.add(resultSet.getString("COLUMN_NAME"));
            }
        }

        return columns;
    }

    private void createMissingColumn(List<String> columns) throws SQLException {
        List<String> existsColumn = getColumns();
        List<String> pendingSQL = new ArrayList<>();
        for (String column : columns) {
            if(!existsColumn.contains(column)){
                pendingSQL.add("alter table "+tableName+" add column "+column+" text;");
            }
        }
        pendingSQL.forEach(sql->{
            try {
                new BufferStatement(sql).prepareStatement(core.getConnection()).execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

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
