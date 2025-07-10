package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import dto.Inventory;
import dto.Machine;

/** Class that represents a connection to the database. */
public final class DbConnection implements AutoCloseable
{
    /** Internal Connection to be wrapped around. */
    private final Connection con;

    /** Constructs a new connection to the given database. */
    public static DbConnection connect(String url, String user, String password)
            throws SQLException
    {
        return new DbConnection(url, user, password);
    }
    
    /** Private constructor. */
    private DbConnection(String url, String user, String password)
            throws SQLException
    {
        System.out.println("[   ] Connecting to database...");
        con = DriverManager.getConnection(url, user, password);
        System.out.println("[ok.]");
    }

    /** Defined by AutoCloseable. */
    @Override
    public void close() throws SQLException {
        System.out.println("[   ] Disconnecting from database...");
        con.close();
        System.out.println("[ok.]");
    }
    
    /** Returns a new query from the given SQL-statement. */
    public Query query(String sql) {
        try {
            return new Statement(con.prepareStatement(sql));
        } catch (SQLException e) {
            return FailedStatement.INSTANCE;
        }
    }
    
    //////////////////////////////////////////////////////////////
    
    private static final Mapper<Machine> MACHINE = rs -> {
        return new Machine(
                rs.getInt("id"),
                rs.getInt("modelId"),
                rs.getString("name"),
                rs.getInt("staffId"),
                rs.getInt("locationId"),
                rs.getString("street"),
                rs.getString("city"),
                rs.getString("country"),
                rs.getInt("balance"),
                rs.getBoolean("isRunning"));
    };
    
    //////////////////////////////////////////////////////////////
    
    public List<Machine> allMachines() {
        return query("""
        SELECT *
        FROM Machine M
        INNER JOIN Location L
                ON M.locationId = L.id
        INNER JOIN MachineModel MM
                ON M.modelId = MM.id
        """).toList(MACHINE);
    }
    
    public Optional<Machine> machineById(int id) {
        var list = query("""
        SELECT *
        FROM Machine M
        INNER JOIN Location L
                ON M.locationId = L.id
        INNER JOIN MachineModel MM
                ON M.modelId = MM.id
        WHERE M.id = ?
        """).withArgs(id).toList(MACHINE);
        
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }
    
    public List<Inventory> inventory(int machineId) {
        return query("""
        SELECT *
        FROM Inventory Inv
        INNER JOIN Item
                ON Inv.itemId = Item.id
        """)
        .toList(rs -> {
            return new Inventory(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getInt("amount"),
                    rs.getInt("price"),
                    rs.getInt("slot"));
        });
    }
    
    public void switchOn(int machineId) {
        query("""
            UPDATE Machine
            SET isRunning = TRUE
            WHERE id = ?;
            """)
        .withArgs(machineId)
        .update();
    }
    
    public void switchOff(int machineId) {
        query("""
            UPDATE Machine
            SET isRunning = FALSE
            WHERE id = ?;
            """)
        .withArgs(machineId)
        .update();
    }
    
    public void buy(int machineId, int itemId) {
        query("""
        UPDATE Inventory
        SET amount = amount - 1
        WHERE machineId = ? AND slot = ? AND amount > 0;

        DELETE FROM Inventory
        WHERE machineId = ? AND slot = ? AND amount = 0;
        """)
        .withArgs(machineId, itemId)
        .update();
    }
}
