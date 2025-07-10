package main;

import static java.util.stream.Collectors.joining;
import static main.StateResult.*;

import java.util.List;
import java.util.Objects;

import dto.DbObject;
import dto.Inventory;
import dto.Machine;
import sql.DbConnection;

/**
 * Vending machine items screen.
 */
public class VendingMachine implements State
{
    private final Machine m;
    private final DbConnection db;
    
    public VendingMachine(Machine m, DbConnection db) {
        this.m = Objects.requireNonNull(m);
        this.db = Objects.requireNonNull(db);
    }

    private boolean firstTime = true;
    
    /**
     * Lazily loaded list of all inventory.
     */
    private List<Inventory> inventory;

    @Override
    public String prompt() {
        if (!firstTime) {
            return "";
        }
        firstTime = false;
        inventory = db.inventory(m.id());

        return """
        %-20s | RUNNING | %s, %s %s
        ------------------------------------------------------
        %s
        ------------------------------------------------------
        Please choose item (by slot):
        Note: "a" is absolutely NOT admin mode or something.

        """.formatted(
            m.modelName(),
            m.street(),
            m.city(),
            m.country(),
            inventory.stream()
                     .map(DbObject::toDisplayString)
                     .collect(joining("\r\n"))
        );
    }

    @Override
    public StateResult handle(String input, DbConnection con) {
        if (input.equalsIgnoreCase("a")) {
            return fail("[ Unimplemented :( ]");
        }
        if (!Util.isNumber(input)) {
            return fail("Expected a number (slot).");
        }
        int id = Integer.parseInt(input);
        var item = inventory.stream().filter(i -> i.slot() == id).findFirst();
        if (item.isEmpty()) {
            return fail("This item does not exist");
        }
        return transition(new Payment(item.get(), m, db));
    }
    
}
